package com.lianjia.matrix.common.command.registry.config;

import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;
import com.lianjia.matrix.common.command.registry.support.DefaultProjectStatusListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 程天亮
 * @Created
 */
@Configuration
@EnableConfigurationProperties(RegistryProperties.class)
public class ComanndRegitryAutoConfig {

    @Autowired
    private RegistryProperties registryProperties;


    @Bean(name = "defaultProjectStatusChangeListener")
    @ConditionalOnMissingBean(ProjectStatusListener.class)
    public ProjectStatusListener defaultProjectStatusChangeListener() {
        return new DefaultProjectStatusListener();
    }

}
