package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;

import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public interface CommandRegistryManager {

    /**
     * 获取所有注册的项目
     *
     * @return
     */
    List<Project> getRegistriedProjects() throws RegistryException;

    /**
     * 剔除某个项目
     *
     * @param project
     * @throws RegistryException
     */
    void removeProject(Project project) throws RegistryException;
}
