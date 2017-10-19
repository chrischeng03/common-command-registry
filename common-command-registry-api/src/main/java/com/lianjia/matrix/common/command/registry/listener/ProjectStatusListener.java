package com.lianjia.matrix.common.command.registry.listener;


import com.lianjia.matrix.common.command.registry.entity.Project;

/**
 * Created by chengtianliang on 2016/10/20.
 */
public interface ProjectStatusListener {

    /**
     * 项目自动注册监听
     *
     * @param project
     */
    void onProjectRegisted(Project project);

    /**
     * 项目退出监听
     *
     * @param project
     */
    void onProjectDetached(Project project);
}
