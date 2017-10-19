package com.lianjia.matrix.common.command.registry.listener;


import com.lianjia.matrix.common.command.registry.entity.Instruction;

/**
 * Created by chengtianliang on 2016/10/20.
 */
public interface InstructionListener {

    /**
     * 接收到指令
     *
     * @param instruction
     */
    void onInstructionRecieved(Instruction instruction);
}
