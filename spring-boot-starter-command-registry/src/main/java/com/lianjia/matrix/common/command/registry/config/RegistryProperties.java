package com.lianjia.matrix.common.command.registry.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author 程天亮
 * @Created
 */
@ConfigurationProperties(prefix = "registry")
public class RegistryProperties {

    private String registryPrefix;

    @NestedConfigurationProperty
    private ProjectProperties project = new ProjectProperties();

    @NestedConfigurationProperty
    private ZkProperties zk = new ZkProperties();

    @NestedConfigurationProperty
    private RedisTopicProperties redisTopic = new RedisTopicProperties();

    public String getRegistryPrefix() {
        return registryPrefix;
    }

    public void setRegistryPrefix(String registryPrefix) {
        this.registryPrefix = registryPrefix;
    }

    public ZkProperties getZk() {
        return zk;
    }

    public void setZk(ZkProperties zk) {
        this.zk = zk;
    }

    public RedisTopicProperties getRedisTopic() {
        return redisTopic;
    }

    public void setRedisTopic(RedisTopicProperties redisTopic) {
        this.redisTopic = redisTopic;
    }

    public ProjectProperties getProject() {
        return project;
    }

    public void setProject(ProjectProperties project) {
        this.project = project;
    }
}
