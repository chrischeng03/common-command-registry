package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public class RedisInstructionSender extends AbstractInstructionSender implements CommandRegistryManager,Wrapperable {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisInstructionSender.class);

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
    public List<Project> getRegistriedProjects() throws RegistryException {
        return getRegistriedProjects();
    }

    @Override
    public void removeProject(Project project) throws RegistryException {
        registryWrapper.detach(project);
    }

    @Override
    public void sendInstruction(Project project, Instruction instruction) throws CommandException {
        registryWrapper.sendInstruction(project, instruction);
    }

    @Override
    public void broadcast(Instruction instruction) throws CommandException {
        List<Project> projects = registryWrapper.registedProjects();
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
        registryWrapper.setProjectStatusListener(this);
        registryWrapper.setInstructionRecieveListener(this);
        registryWrapper.init();
        Scheduled.schedue(new Runnable() {
            @Override
            public void run() {
                try {
                    registry();
                } catch (Throwable e) {
                    LOGGER.info("[Registry Error]", e);
                }
            }
        });
    }

    @Override
    public void destroy() throws LifecycleException {
        registryWrapper.destroy();
    }
}
