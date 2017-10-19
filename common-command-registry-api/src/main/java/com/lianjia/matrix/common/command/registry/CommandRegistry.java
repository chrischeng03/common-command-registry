package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;

/**
 * @author 程天亮
 * 命令注册器，通过命令注册、注销、发布命令、接收命令，可以将命令（指令）传达到对指令感兴趣的工程
 */
public interface CommandRegistry {

    /**
     * 注册Project，实现方需要可以释放允许注册
     *
     * @param project
     * @throws RegistryException
     */
    void registry(Project project) throws RegistryException;

    /**
     * 注册
     * @throws RegistryException
     */
    void registry() throws RegistryException;

    /**
     * 注销
     *
     * @param project
     * @throws RegistryException
     */
    void detach(Project project) throws RegistryException;

    /**
     * 注销
     * @throws RegistryException
     */
    void detach() throws RegistryException;

}
