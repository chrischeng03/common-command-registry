package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.listener.InstructionListener;

/**
 *
 */
public interface InstructionRecieveableCommandRegistry extends CommandRegistry {

    /**
     * 注册指令接收回调
     *
     * @param listener
     */
    void addInstructionRecieveListener(InstructionListener listener);
}
