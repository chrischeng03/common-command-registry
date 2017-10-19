package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 程天亮
 * @Created
 */
public class RedisClientRegistry extends AbstractClientCommandRegistry implements Wrapperable{

    private RegistryWrapper registryWrapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisClientRegistry.class);

    public void setRegistryWrapper(RegistryWrapper registryWrapper) {
        this.registryWrapper = registryWrapper;
    }

    @Override
    protected void doRegistry(Project project) throws RegistryException {
        registryWrapper.registry(project);
    }

    @Override
    protected void doDetach(Project project) throws RegistryException {
        registryWrapper.detach(project);
    }

    @Override
    public void init() throws LifecycleException {
        if (registryWrapper != null) {
            registryWrapper.setInstructionRecieveListener(this);
        }
        registryWrapper.init();
        Scheduled.schedue(new Runnable() {
            @Override
            public void run() {
                try {
                    registry();
                } catch (Throwable e) {
                    LOGGER.info("[Registry Error]", e);
                }
            }
        });
    }

    @Override
    public void destroy() throws LifecycleException {
        registryWrapper.destroy();
    }
}
