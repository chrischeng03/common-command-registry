package com.lianjia.matrix.common.command.registry.config;

import com.lianjia.matrix.common.command.registry.InstructionSender;
import com.lianjia.matrix.common.command.registry.RedisRegistryWrapper;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author 程天亮
 * @Created
 */
@Configuration
public class TopicConfig {

    @Autowired(required = false)
    private RegistryWrapper redisRegistryWrapper;

    @Autowired
    private ApplicationContext context;

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer() {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        if (null != redisRegistryWrapper) {
            if (redisRegistryWrapper instanceof RedisRegistryWrapper) {
                redisMessageListenerContainer.setConnectionFactory(((RedisRegistryWrapper) redisRegistryWrapper).getRedisTemplate().getConnectionFactory());
                RedisRegistryWrapper rrw = (RedisRegistryWrapper) redisRegistryWrapper;
                MessageListener messageListener = rrw.getMessageListener();
                if (isBossSide()) {
                    redisMessageListenerContainer.addMessageListener(messageListener, new PatternTopic(rrw.getRedisTopicProperties().getProjectDetachedTopic()));
                    redisMessageListenerContainer.addMessageListener(messageListener, new PatternTopic(rrw.getRedisTopicProperties().getProjectRegistedTopic()));
                }
                redisMessageListenerContainer.addMessageListener(messageListener, new PatternTopic(rrw.getRedisTopicProperties().getInstructionTopic() + "_*"));
            }
        }
        return redisMessageListenerContainer;
    }


    private boolean isBossSide() {
        String[] names = context.getBeanNamesForType(InstructionSender.class);
        if (null != names && names.length > 0) {
            return true;
        }
        return false;
    }
}
