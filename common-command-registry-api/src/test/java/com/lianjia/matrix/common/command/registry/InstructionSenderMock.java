package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;

/**
 * @author 程天亮
 * @Created
 */
public class InstructionSenderMock implements InstructionSender{
    @Override
    public void sendInstruction(Project project, Instruction instruction) throws CommandException {

    }

    @Override
    public void broadcast(Instruction instruction) throws CommandException {

    }

    @Override
    public void broadcast(Project[] projects, Instruction instruction) throws CommandException {

    }

    @Override
    public void addInstructionRecieveListener(InstructionListener listener) {

    }

    @Override
    public void registry(Project project) throws RegistryException {

    }

    @Override
    public void registry() throws RegistryException {

    }

    @Override
    public void detach(Project project) throws RegistryException {

    }

    @Override
    public void detach() throws RegistryException {

    }
}
