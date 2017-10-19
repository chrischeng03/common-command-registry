package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.beandefinition.ObjectMeta;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectProperty;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectPropertyMark;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public class RedisCommandRegistryProcessor implements CommandRegistryProcessor {

    private static final String REDIS_PREFIX = "redis";

    @Override
    public String prefix() {
        return REDIS_PREFIX;
    }

    @Override
    public ObjectMeta<? extends InstructionSender> createInstructionSender(Project project) {
        List<ObjectPropertyMark> propertyMarks = new ArrayList<>();
        propertyMarks.add(new ObjectPropertyMark("registryWrapper", "registryWrapper"));
        List<ObjectProperty> properties = new ArrayList<>();
        properties.add(new ObjectProperty("selfProject", project));
        return new ObjectMeta<>(RedisInstructionSender.class, null, propertyMarks, properties);
    }

    @Override
    public ObjectMeta<? extends ClientCommandRegistry> createClientRegistry(Project project) {
        List<ObjectPropertyMark> propertyMarks = new ArrayList<>();
        propertyMarks.add(new ObjectPropertyMark("registryWrapper", "registryWrapper"));
        List<ObjectProperty> properties = new ArrayList<>();
        properties.add(new ObjectProperty("selfProject", project));
        return new ObjectMeta<>(RedisClientRegistry.class, null, propertyMarks, properties);
    }

    @Override
    public ObjectMeta<? extends RegistryWrapper> createRegistryWrapper(Project project) {
        List<ObjectPropertyMark> propertyMarks = new ArrayList<>();
        propertyMarks.add(new ObjectPropertyMark("redisTopicProperties", "redisTopicProperties"));
        propertyMarks.add(new ObjectPropertyMark("redisTemplate", "topicRedisTemplate"));
        return new ObjectMeta<>(RedisRegistryWrapper.class, null, propertyMarks, null);
    }


    @Override
    public int getOrder() {
        return 100;
    }
}
