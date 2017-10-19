package com.lianjia.matrix.common.command.registry;

import com.alibaba.fastjson.JSON;
import com.lianjia.matrix.common.command.registry.config.RedisTopicProperties;
import com.lianjia.matrix.common.command.registry.entity.Instruction;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.CommandException;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author 程天亮
 * @Created
 */
public class RedisRegistryWrapper implements RegistryWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryWrapper.class);

    private static final String PROJECT_REGISTRIED_NAME = "com.lianjia.matrix.common.command.registry.projects";

    private RedisTopicProperties redisTopicProperties;

    private InstructionListener instructionListener;

    private ProjectStatusListener projectStatusListener;

    private MessageListener messageListener;

    private RedisTemplate<String, byte[]> redisTemplate;

    public RedisRegistryWrapper() {
    }

    public void setRedisTemplate(RedisTemplate<String, byte[]> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTopicProperties getRedisTopicProperties() {
        return redisTopicProperties;
    }

    public void setRedisTopicProperties(RedisTopicProperties redisTopicProperties) {
        this.redisTopicProperties = redisTopicProperties;
    }

    @Override
    public void registry(Project project) throws CommandException {
//        redisTemplate.opsForHash().put(PROJECT_REGISTRIED_NAME, project.getCode(), JSON.toJSONBytes(project));
        redisTemplate.opsForValue().set(PROJECT_REGISTRIED_NAME + "_" + project.getCode(), JSON.toJSONBytes(project), Scheduled.DEFAULT_EXPIRE_REGISTRY_IN_SEC, TimeUnit.SECONDS);
        redisTemplate.convertAndSend(redisTopicProperties.getProjectRegistedTopic(), JSON.toJSONBytes(project));
    }

    @Override
    public void detach(Project project) throws CommandException {
        redisTemplate.delete(PROJECT_REGISTRIED_NAME + "_" + project.getCode());
        redisTemplate.convertAndSend(redisTopicProperties.getProjectDetachedTopic(), JSON.toJSONBytes(project));
    }

    @Override
    public void setInstructionRecieveListener(InstructionListener instructionRecieveListener) {
        this.instructionListener = instructionRecieveListener;
    }

    @Override
    public void sendInstruction(Project project, Instruction instruction) throws CommandException {
        redisTemplate.convertAndSend(redisTopicProperties.getInstructionTopic() + "_" + project.getCode(), instruction.toBytes());
    }

    @Override
    public List<Project> registedProjects() throws RegistryException {
        Set<String> keys = scan(PROJECT_REGISTRIED_NAME + "_*");
        List<Project> projects = new ArrayList<>();
        try {
            for (String key : keys) {
                byte[] content = redisTemplate.opsForValue().get(key);
                if (null != content) {
                    try {
                        Project project = JSON.parseObject(content, Project.class);
                        projects.add(project);
                    } catch (Exception e) {
                        LOGGER.info("[Parse Error from Json:{}]", new String(content), e);
                    }

                }
            }
        } catch (Exception e) {
            throw new RegistryException(e);
        }
        return projects;
    }

    @Override
    public void init() throws LifecycleException {
        messageListener = new MessageListener() {
            @Override
            public void onMessage(Message message, byte[] pattern) {
                String topic = new String(message.getChannel());
                byte[] body = message.getBody();
                RedisSerializer<?> serializer = redisTemplate.getValueSerializer();
                if (null != serializer) {
                    body = (byte[]) serializer.deserialize(body);
                }
                if (projectStatusListener != null) {
                    Project project = JSON.parseObject(new String(body, Charset.forName("utf8")), Project.class);
                    if (topic.equals(redisTopicProperties.getProjectRegistedTopic())) {
                        projectStatusListener.onProjectRegisted(project);
                    } else if (topic.equals(redisTopicProperties.getProjectDetachedTopic())) {
                        projectStatusListener.onProjectDetached(project);
                    }
                }
                if (instructionListener != null && topic.startsWith(redisTopicProperties.getInstructionTopic() + "_")) {
                    Instruction instruction = Instruction.from(body);
                    instructionListener.onInstructionRecieved(instruction);
                }
            }
        };
    }

    @Override
    public void destroy() throws LifecycleException {

    }


    public void setProjectStatusListener(ProjectStatusListener projectStatusListener) {
        this.projectStatusListener = projectStatusListener;
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public RedisTemplate<String, byte[]> getRedisTemplate() {
        return redisTemplate;
    }

    public Set<String> scan(String pattern) {
        Set<String> keys = new LinkedHashSet<>();
        RedisConnection redisConnection = null;
        try {
            redisConnection = redisTemplate.getConnectionFactory().getConnection();
            ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
            Cursor<byte[]> c = redisConnection.scan(options);
            while (c.hasNext()) {
                keys.add(new String(c.next()));
            }
        } finally {
            redisConnection.close(); //Ensure closing this connection.
        }
        return keys;
    }
}
