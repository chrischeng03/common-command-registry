package com.lianjia.matrix.common.command.registry.config;

import com.lianjia.matrix.common.command.registry.*;
import com.lianjia.matrix.common.command.registry.listener.InstructionListener;
import com.lianjia.matrix.common.command.registry.listener.ProjectStatusListener;
import com.lianjia.matrix.common.command.registry.support.AuthHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author 程天亮
 * @Created
 */
@Component
public class ComanndRegistryConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String[] names = applicationContext.getBeanNamesForType(ProjectStatusChangeWatchable.class);
        if (names != null && names.length > 0) {
            initProjectStatusChangeWatchabls(applicationContext);
        }

        names = applicationContext.getBeanNamesForType(InstructionRecieveableCommandRegistry.class);
        if (names != null && names.length > 0) {
            initInstructionRecieveableCommandRegistries(applicationContext);
        }
        initAuthHandler(applicationContext);
    }

    private void initAuthHandler(ApplicationContext context) {
        String[] authHandlers = context.getBeanNamesForType(AuthHandler.class);
        if (authHandlers != null && authHandlers.length > 0) {
            AuthHandler authHandler = context.getBean(AuthHandler.class);
            String[] bossNames = context.getBeanNamesForType(AbstractInstructionSender.class);
            if (bossNames != null && bossNames.length > 0) {
                for (String bossName : bossNames) {
                    AbstractInstructionSender abstractInstructionSender = context.getBean(bossName, AbstractInstructionSender.class);
                    abstractInstructionSender.setAuthHandler(authHandler);
                }
            }
            String[] clientNames = context.getBeanNamesForType(AbstractClientCommandRegistry.class);
            if (clientNames != null && clientNames.length > 0) {
                for (String clientName : clientNames) {
                    AbstractClientCommandRegistry abstractClientCommandRegistry = context.getBean(clientName, AbstractClientCommandRegistry.class);
                    abstractClientCommandRegistry.setAuthHandler(authHandler);
                }
            }
        }

    }

    private void initInstructionRecieveableCommandRegistries(ApplicationContext context) {
        Map<String, InstructionRecieveableCommandRegistry> instructionRecieveableCommandRegistryMap = context.getBeansOfType(InstructionRecieveableCommandRegistry.class);
        for (Map.Entry<String, InstructionRecieveableCommandRegistry> entry : instructionRecieveableCommandRegistryMap.entrySet()) {
            initInstructionRecievebaleCommandRegistry(context, entry.getValue());
        }
    }

    private void initProjectStatusChangeWatchabls(ApplicationContext context) {
        Map<String, ProjectStatusChangeWatchable> projectStatusChangeWatchableMap = context.getBeansOfType(ProjectStatusChangeWatchable.class);
        for (Map.Entry<String, ProjectStatusChangeWatchable> entry : projectStatusChangeWatchableMap.entrySet()) {
            initProjectStatusChangeWatchable(context, entry.getValue());
        }
    }

    private void initProjectStatusChangeWatchable(ApplicationContext context, ProjectStatusChangeWatchable projectStatusChangeWatchable) {
        Map<String, ProjectStatusListener> listenerMap = context.getBeansOfType(ProjectStatusListener.class);
        for (Map.Entry<String, ProjectStatusListener> entry : listenerMap.entrySet()) {
            ProjectStatusListener listener = entry.getValue();
            if (listener instanceof CommandRegistry) {
                continue;
            }
            projectStatusChangeWatchable.addProjectStatusChangeListener(listener);
        }
    }

    private void initInstructionRecievebaleCommandRegistry(ApplicationContext context, InstructionRecieveableCommandRegistry instructionRecieveableCommandRegistry) {
        Map<String, InstructionListener> listenerMap = context.getBeansOfType(InstructionListener.class);
        for (Map.Entry<String, InstructionListener> entry : listenerMap.entrySet()) {
            InstructionListener listener = entry.getValue();
            if (listener instanceof CommandRegistry) {
                continue;
            }
            instructionRecieveableCommandRegistry.addInstructionRecieveListener(listener);
        }
    }
}
