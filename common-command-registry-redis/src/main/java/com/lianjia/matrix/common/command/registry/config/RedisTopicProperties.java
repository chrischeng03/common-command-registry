package com.lianjia.matrix.common.command.registry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author 程天亮
 * @Createdr
 */
@Component("redisTopicProperties")
public class RedisTopicProperties {

    @Value("${registry.redis-topic.project-registed-topic:}")
    private String projectRegistedTopic;

    @Value("${registry.redis-topic.project-detached-topic:}")
    private String projectDetachedTopic;

    @Value("${registry.redis-topic.instruction-topic:}")
    private String instructionTopic;

    public String getProjectRegistedTopic() {
        return projectRegistedTopic;
    }

    public void setProjectRegistedTopic(String projectRegistedTopic) {
        this.projectRegistedTopic = projectRegistedTopic;
    }

    public String getProjectDetachedTopic() {
        return projectDetachedTopic;
    }

    public void setProjectDetachedTopic(String projectDetachedTopic) {
        this.projectDetachedTopic = projectDetachedTopic;
    }

    public String getInstructionTopic() {
        return instructionTopic;
    }

    public void setInstructionTopic(String instructionTopic) {
        this.instructionTopic = instructionTopic;
    }
}
