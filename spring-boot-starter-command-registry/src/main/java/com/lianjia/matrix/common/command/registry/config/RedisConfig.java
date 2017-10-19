package com.lianjia.matrix.common.command.registry.config;

import com.lianjia.matrix.common.command.registry.RedisRegistryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import redis.clients.jedis.JedisPoolConfig;

import java.io.UnsupportedEncodingException;

/**
 * @author 程天亮
 * @Created
 */
@Configuration
@EnableConfigurationProperties({RegistryProperties.class,RedisProperties.class})
@ConditionalOnClass(RedisRegistryWrapper.class)
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;

    @Autowired
    private RegistryProperties registryProperties;

    @Bean
    @ConditionalOnMissingBean
    public JedisConnectionFactory jedisConnectionFactory(){
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setClientName(registryProperties.getProject().getCode());
        configConnectionFactory(jedisConnectionFactory, redisProperties);
        return jedisConnectionFactory;
    }

    @Bean(name = "topicRedisTemplate")
    public RedisTemplate<String, byte[]> redisTemplate(JedisConnectionFactory factory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new RedisSerializer<String>() {
            @Override
            public byte[] serialize(String s) throws SerializationException {
                try {
                    return s.getBytes("utf8");
                } catch (UnsupportedEncodingException e) {
                }
                return new byte[0];
            }

            @Override
            public String deserialize(byte[] bytes) throws SerializationException {
                return new String(bytes);
            }
        });
        redisTemplate.setValueSerializer(new RedisSerializer<byte[]>() {
            @Override
            public byte[] serialize(byte[] s) throws SerializationException {
                return s;
            }

            @Override
            public byte[] deserialize(byte[] bytes) throws SerializationException {
                return bytes;
            }
        });
        return redisTemplate;
    }

    private void configConnectionFactory(JedisConnectionFactory jedisConnectionFactory, RedisProperties redisProperties) {
        jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setUsePool(true);
        jedisConnectionFactory.setTimeout(redisProperties.getTimeout());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setLifo(true);
        poolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
        poolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
        poolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());
        poolConfig.setMinEvictableIdleTimeMillis(1000);
        poolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        jedisConnectionFactory.setPoolConfig(poolConfig);
    }

}
