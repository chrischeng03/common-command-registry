package com.lianjia.matrix.common.command.registry.anno;

import com.lianjia.matrix.common.command.registry.*;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectConstruct;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectMeta;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectProperty;
import com.lianjia.matrix.common.command.registry.beandefinition.ObjectPropertyMark;
import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Constructor;
import java.util.*;

/**
 * @author 程天亮
 * @Created
 */
public class RegistryImportor implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryImportor.class);

    private static final String[] REGISTRY_PREFIX = {"registry.registryPrefix", "registry.registry-prefix"};

    private static final String PROJECT_NAME = "registry.project.name";

    private static final String PROJECT_CODE = "registry.project.code";

    private static final String PROJECT_APPKEY = "registry.project.appkey";

    private static final String PROJECT_PATH = "registry.project.path";

    private static final String PROJECT_NODE = "registry.project.node";

    private static final String PROJECT_ADDRESS = "registry.project.address";

    private Project selfProject;

    private String prefix;

    private List<CommandRegistryProcessor> prefixProcessors;

    @Override
    public void setEnvironment(Environment environment) {
        for (String prefixTag : REGISTRY_PREFIX) {
            String prefix = environment.getProperty(prefixTag);
            if (prefix != null) {
                this.prefix = prefix;
                break;
            }
        }
        prefixProcessors = getRegistryPrefixProcessors();
        if (prefixProcessors == null || prefixProcessors.size() == 0) {
            LOGGER.warn("[No PrefixProcessor found]");
            throw new RuntimeException("No PrefixProcessor found");
        }
        if (prefix == null) {
            CommandRegistryProcessor defaultProcessor = prefixProcessors.get(0);
            prefix = defaultProcessor.prefix();
        } else {
            if (!isPrefixSupported(prefix, prefixProcessors)) {
                throw new RuntimeException("Prefix:" + prefix + " not Supported");
            }
        }
        this.selfProject = createProject(environment);
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        if (metadata.hasAnnotation(EnableBossRegistry.class.getName())) {
            registryObjects(true, registry);
        } else if (metadata.hasAnnotation(EnableClientRegistry.class.getName())) {
            registryObjects(false, registry);
        }

    }

    private Project createProject(Environment environment) {
        Project project = new Project();
        String projectName = environment.getProperty(PROJECT_NAME);
        project.setName(projectName);
        String projectCode = environment.getProperty(PROJECT_CODE);
        project.setCode(projectCode);
        String projectPath = environment.getProperty(PROJECT_PATH);
        project.setPath(projectPath);
        String projectNode = environment.getProperty(PROJECT_NODE);
        project.setNode(projectNode);
        String projectAddress = environment.getProperty(PROJECT_ADDRESS);
        project.setAddress(projectAddress);
        String projectAppkey = environment.getProperty(PROJECT_APPKEY);
        String key = project.getAddress() + project.getCode();
        String authCode = DigestUtils.md5Hex(key + projectAppkey);
        project.setAuthCode(authCode);
        return project;
    }

//    private String convertKey2CamelCase(String key) {
//        int index = -1;
//        String converted = key;
//        while ((index = converted.indexOf("_")) != -1 && index != key.length() - 1) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(key.substring(0, index));
//            sb.append(key.substring(index + 1, index + 2).toUpperCase());
//            if (index < key.length() - 2) {
//                sb.append(key.substring(index + 2));
//            }
//            converted = sb.toString();
//        }
//        return converted;
//    }

//    private boolean isBossClient(AnnotationMetadata metadata) {
//
//        List<Class<?>> types = collectClasses(attributes.get("value"));
//        for (Class<?> type : types) {
//            EnableBossRegistry enableBossRegistry =
//        }
//    }

    private List<Class<?>> collectClasses(List<Object> list) {
        ArrayList<Class<?>> result = new ArrayList<Class<?>>();
        for (Object object : list) {
            for (Object value : (Object[]) object) {
                if (value instanceof Class && value != void.class) {
                    result.add((Class<?>) value);
                }
            }
        }
        return result;
    }


    private void registryObjects(boolean isBossSide, BeanDefinitionRegistry registry) {
        CommandRegistryProcessor prefixProcessor = findPrefixProcessor();
        if (isBossSide) {
            ObjectMeta<? extends InstructionSender> instructionSenderObjectMeta = prefixProcessor.createInstructionSender(selfProject);
            registryBoss(instructionSenderObjectMeta, registry);
        } else {
            ObjectMeta<? extends ClientCommandRegistry> clientCommandRegistryObjectMeta = prefixProcessor.createClientRegistry(selfProject);
            registryClient(clientCommandRegistryObjectMeta, registry);
        }

        ObjectMeta<? extends RegistryWrapper> registryWrapperObjectMeta = prefixProcessor.createRegistryWrapper(selfProject);
        registryWrapper(registryWrapperObjectMeta,registry);
    }

    private CommandRegistryProcessor findPrefixProcessor() {
        for (CommandRegistryProcessor prefixProcessor : prefixProcessors) {
            if (prefixProcessor.prefix().equals(prefix)) return prefixProcessor;
        }
        return null;
    }

    private void registryWrapper(ObjectMeta<? extends RegistryWrapper> meta,BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder wrapperBuilder = createBeanDefinitionBuilder(meta);
        registry.registerBeanDefinition("registryWrapper", wrapperBuilder.getRawBeanDefinition());
    }

    private BeanDefinitionBuilder createBeanDefinitionBuilder(ObjectMeta<?> meta) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(meta.getTargetClass());
        configBeanDefinition(meta, beanDefinitionBuilder);
        return beanDefinitionBuilder;
    }

    private void configBeanDefinition(ObjectMeta<?> meta,  BeanDefinitionBuilder beanDefinitionBuilder) {
        ObjectConstruct objectConstruct = meta.getObjectConstruct();
        if (null != objectConstruct) {
            for (Object arg : objectConstruct.getArgs()) {
                beanDefinitionBuilder.addConstructorArgValue(arg);
            }
        }
        List<ObjectPropertyMark> objectPropertyMarks = meta.getPropertyMarks();
        if (null != objectPropertyMarks) {
            for (ObjectPropertyMark objectPropertyMark : objectPropertyMarks) {
                beanDefinitionBuilder.addPropertyReference(objectPropertyMark.getPropertyName(),objectPropertyMark.getPropertyValueMarkName());
            }
        }


        List<ObjectProperty> objectProperties = meta.getProperties();
        if (null != objectProperties) {
            for (ObjectProperty property : objectProperties) {
                beanDefinitionBuilder.addPropertyValue(property.getPropertyName(),property.getValue());
            }
        }
    }

    private void registryBoss(ObjectMeta<? extends CommandRegistry> meta, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder instructionSenderBuilder =
                BeanDefinitionBuilder.
                        genericBeanDefinition(meta.getTargetClass()).
                        setInitMethodName("init")
                        .setDestroyMethodName("destroy");
       configBeanDefinition(meta,instructionSenderBuilder);
        registry.registerBeanDefinition(prefix + RegistryConstants.INSTRUCTION_SENDER, instructionSenderBuilder.getRawBeanDefinition());
    }

    private void registryClient(ObjectMeta<? extends CommandRegistry> meta, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder clientBuilder = BeanDefinitionBuilder.genericBeanDefinition(meta.getTargetClass())
                .setInitMethodName("init")
                .setDestroyMethodName("destroy");
        configBeanDefinition(meta,clientBuilder);
        registry.registerBeanDefinition(prefix + RegistryConstants.CLIENT_COMMAND_REGISTRY_NAME, clientBuilder.getRawBeanDefinition());
    }

    private boolean isPrefixSupported(String prefix, List<CommandRegistryProcessor> prefixProcessors) {
        for (CommandRegistryProcessor prefixProcessor : prefixProcessors) {
            if (prefixProcessor.prefix().equals(prefix)) return true;
        }

        return false;
    }

    private List<CommandRegistryProcessor> getRegistryPrefixProcessors() {
        Collection<CommandRegistryProcessor> prefixProcessors = (Collection<CommandRegistryProcessor>) getSpringFactoriesInstances(CommandRegistryProcessor.class);
        List<CommandRegistryProcessor> prefixProcessorList = new ArrayList<>(prefixProcessors);
        Collections.sort(prefixProcessorList, new Comparator<CommandRegistryProcessor>() {
            @Override
            public int compare(CommandRegistryProcessor o1, CommandRegistryProcessor o2) {
                return o1.getOrder() - o2.getOrder();
            }
        });
        return prefixProcessorList;
    }

    private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type) {
        return getSpringFactoriesInstances(type, new Class<?>[]{});
    }

    private <T> Collection<? extends T> getSpringFactoriesInstances(Class<T> type,
                                                                    Class<?>[] parameterTypes, Object... args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        // Use names and ensure unique to protect against duplicates
        Set<String> names = new LinkedHashSet<String>(
                SpringFactoriesLoader.loadFactoryNames(type, classLoader));
        List<T> instances = createSpringFactoriesInstances(type, parameterTypes,
                classLoader, args, names);
        AnnotationAwareOrderComparator.sort(instances);
        return instances;
    }


    @SuppressWarnings("unchecked")
    private <T> List<T> createSpringFactoriesInstances(Class<T> type,
                                                       Class<?>[] parameterTypes, ClassLoader classLoader, Object[] args,
                                                       Set<String> names) {
        List<T> instances = new ArrayList<T>(names.size());
        for (String name : names) {
            try {
                Class<?> instanceClass = ClassUtils.forName(name, classLoader);
                Assert.isAssignable(type, instanceClass);
                Constructor<?> constructor = instanceClass.getConstructor(parameterTypes);
                T instance = (T) constructor.newInstance(args);
                instances.add(instance);
            } catch (Throwable ex) {
                throw new IllegalArgumentException(
                        "Cannot instantiate " + type + " : " + name, ex);
            }
        }
        return instances;
    }
}
