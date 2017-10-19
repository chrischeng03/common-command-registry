package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.AuthErrorException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import com.lianjia.matrix.common.command.registry.support.AuthHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chengtianliang
 * 抽象Command
 */
public abstract class AbstractClientCommandRegistry implements ClientCommandRegistry, Lifecycleable, InstructionListener {

    protected List<InstructionListener> instructionRecieveListeners = new ArrayList<>();

    private AuthHandler authHandler;

    protected Project selfProject;

    public AbstractClientCommandRegistry() {
    }

    public void setSelfProject(Project selfProject) {
        this.selfProject = selfProject;
    }

    public void setAuthHandler(AuthHandler authHandler) {
        this.authHandler = authHandler;
    }

    @Override
    public void addInstructionRecieveListener(InstructionListener listener) {
        if (listener != null) {
            instructionRecieveListeners.add(listener);
        }
    }

    @Override
    public void registry(Project project) throws RegistryException {
        if (authHandler != null ) {
            try {
                authHandler.auth(project);
            } catch (AuthErrorException e) {
                throw new RegistryException(e);
            }
        }
        doRegistry(project);
    }

    @Override
    public void detach(Project project) throws RegistryException {
        if (authHandler != null ) {
            try {
                authHandler.auth(project);
            } catch (AuthErrorException e) {
                throw new RegistryException(e);
            }
        }
        doDetach(project);
    }

    @Override
    public void registry() throws RegistryException {
        registry(selfProject);
    }

    @Override
    public void detach() throws RegistryException {
        detach(selfProject);
    }

    @Override
    public void onInstructionRecieved(Instruction instruction) {
        for (InstructionListener listener : instructionRecieveListeners) {
            listener.onInstructionRecieved(instruction);
        }
    }

    public Project getSelfProject() {
        return selfProject;
    }

    protected abstract void doRegistry(Project project) throws RegistryException;

    protected abstract void doDetach(Project project) throws RegistryException;
}
