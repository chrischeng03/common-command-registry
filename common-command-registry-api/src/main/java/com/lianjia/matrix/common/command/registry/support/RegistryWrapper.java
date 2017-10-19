package com.lianjia.matrix.common.command.registry.support;

import com.lianjia.matrix.common.command.registry.Lifecycleable;
import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;

import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public interface RegistryWrapper extends Lifecycleable {

    /**
     * @param project
     * @throws CommandException
     */
    void registry(Project project) throws CommandException;

    /**
     * @param project
     * @throws CommandException
     */
    void detach(Project project) throws CommandException;

    /**
     * @param instructionRecieveListener
     */
    void setInstructionRecieveListener(InstructionListener instructionRecieveListener);

    /**
     * @param project
     * @param instruction
     * @throws CommandException
     */
    void sendInstruction(Project project, Instruction instruction) throws CommandException;

    /**
     * @return
     * @throws RegistryException
     */
    List<Project> registedProjects() throws RegistryException;

    /**
     * @param listener
     */
    void setProjectStatusListener(ProjectStatusListener listener);
}
