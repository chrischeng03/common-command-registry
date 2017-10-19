package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;

import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public class ZkInstructionSender extends AbstractInstructionSender implements CommandRegistryManager,Wrapperable {

    private RegistryWrapper registryWrapper;

    public void setRegistryWrapper(RegistryWrapper registryWrapper) {
        this.registryWrapper = registryWrapper;
    }

    @Override
    protected void doRegistry(Project project) throws RegistryException {
        registryWrapper.registry(project);
    }

    @Override
    protected void doDetach(Project project) throws RegistryException {
        registryWrapper.detach(project);
    }

    @Override
    public void sendInstruction(Project project, Instruction instruction) throws CommandException {
        registryWrapper.sendInstruction(project, instruction);
    }

    @Override
    public void broadcast(Instruction instruction) throws CommandException {
        List<Project> projects = getRegistriedProjects();
        broadcast(projects.toArray(new Project[projects.size()]), instruction);
    }

    @Override
    public void broadcast(Project[] projects, Instruction instruction) throws CommandException {
        for (Project project : projects) {
            sendInstruction(project, instruction);
        }
    }

    @Override
    public void init() throws LifecycleException {
        if (null != this.registryWrapper) {
            this.registryWrapper.setInstructionRecieveListener(this);
            this.registryWrapper.setProjectStatusListener(this);
            if (registryWrapper instanceof ZkRegistryWrapper) {
                ((ZkRegistryWrapper) registryWrapper).setBossSide(true);
            }
        }
        registryWrapper.init();
    }

    @Override
    public void destroy() throws LifecycleException {
        registryWrapper.destroy();
    }

    @Override
    public List<Project> getRegistriedProjects() throws RegistryException {
        return registryWrapper.registedProjects();
    }

    @Override
    public void removeProject(Project project) throws RegistryException {
        registryWrapper.detach(project);
    }
}

