package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;
import com.lianjia.matrix.common.command.registry.exceptions.RegistryException;
import com.lianjia.matrix.common.command.registry.support.RegistryWrapper;

/**
 * @author 程天亮
 * @Created
 */
public class ZkClientRegistry extends AbstractClientCommandRegistry implements Wrapperable {

    private RegistryWrapper registryWrapper;

    public void setRegistryWrapper(RegistryWrapper register) {
        this.registryWrapper = register;
        this.registryWrapper.setInstructionRecieveListener(this);
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
        registryWrapper.init();
    }

    @Override
    public void destroy() throws LifecycleException {
        registryWrapper.destroy();
    }

}
