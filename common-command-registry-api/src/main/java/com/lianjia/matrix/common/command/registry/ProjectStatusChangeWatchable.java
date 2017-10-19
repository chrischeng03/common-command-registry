package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;

/**
 * @author 程天亮
 * @Created
 */
public interface ProjectStatusChangeWatchable {

    /**
     * 添加项目状态变化
     *
     * @param listener
     */
    void addProjectStatusChangeListener(ProjectStatusListener listener);
}
