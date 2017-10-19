package com.lianjia.matrix.common.command.registry.support;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 程天亮
 * @Created
 */
public class DefaultProjectStatusListener implements ProjectStatusListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultProjectStatusListener.class);

    @Override
    public void onProjectRegisted(Project project) {
        LOGGER.info("[项目：{}-{}-{},注册成功]", project.getName(), project.getCode(), project.getNode());
    }

    @Override
    public void onProjectDetached(Project project) {
        LOGGER.info("[项目：{}-{}-{},注销成功]", project.getName(), project.getCode(), project.getNode());
    }
}
