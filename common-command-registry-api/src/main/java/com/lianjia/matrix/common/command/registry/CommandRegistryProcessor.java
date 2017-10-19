package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.beandefinition.ObjectMeta;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.springframework.core.Ordered;

/**
 * @author 程天亮
 * @Created
 */
public interface CommandRegistryProcessor extends Ordered {

    /**
     * CommandRegistry前置，即使用何种注册方式
     *
     * @return 注册方式，也是bean的前置
     */
    String prefix();

    /**
     *
     * @return
     */
    ObjectMeta<? extends InstructionSender> createInstructionSender(Project project);

    /**
     *
     * @return
     */
    ObjectMeta<? extends ClientCommandRegistry> createClientRegistry(Project project);

    ObjectMeta<? extends RegistryWrapper> createRegistryWrapper(Project project);

}
