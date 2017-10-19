package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;

/**
 * 指令发送者
 */
public interface InstructionSender extends InstructionRecieveableCommandRegistry {

    /**
     * 指定Project 发送指令
     *
     * @param project
     * @param instruction
     */
    void sendInstruction(Project project, Instruction instruction) throws CommandException;


    /**
     * 向所有注册的Project广播指令
     *
     * @param instruction
     * @throws CommandException
     */
    void broadcast(Instruction instruction) throws CommandException;

    /**
     * 指定Projects广播指令
     *
     * @param projects
     * @param instruction
     * @throws CommandException
     */
    void broadcast(Project[] projects, Instruction instruction) throws CommandException;
}
