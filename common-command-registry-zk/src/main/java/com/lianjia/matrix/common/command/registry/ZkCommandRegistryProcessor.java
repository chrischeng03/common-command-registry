package com.lianjia.matrix.common.command.registry;

import com.lianjia.matrix.common.command.registry.beandefinition.ObjectConstruct;
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
public class ZkCommandRegistryProcessor implements CommandRegistryProcessor {

    private static final String ZK_PREFIX = "zk";

    @Override
    public String prefix() {
        return ZK_PREFIX;
    }

    @Override
    public ObjectMeta<? extends InstructionSender> createInstructionSender(Project project) {
        List<ObjectPropertyMark> objectPropertyMarks = new ArrayList<>();
        objectPropertyMarks.add(new ObjectPropertyMark("registryWrapper", "registryWrapper"));
        List<ObjectProperty> properties = new ArrayList<>();
        properties.add(new ObjectProperty("selfProject",project));
        return new ObjectMeta<>(ZkInstructionSender.class,null, objectPropertyMarks,properties);
    }

    @Override
    public ObjectMeta<? extends ClientCommandRegistry> createClientRegistry(Project project) {
        List<ObjectPropertyMark> objectPropertyMarks = new ArrayList<>();
        objectPropertyMarks.add(new ObjectPropertyMark("registryWrapper", "registryWrapper"));
        List<ObjectProperty> properties = new ArrayList<>();
        properties.add(new ObjectProperty("selfProject",project));
        return new ObjectMeta<>(ZkClientRegistry.class,null,objectPropertyMarks,properties);
    }

    @Override
    public ObjectMeta<? extends RegistryWrapper> createRegistryWrapper(Project project) {
        List<ObjectPropertyMark> objectPropertyMarkList = new ArrayList<>();
        objectPropertyMarkList.add(new ObjectPropertyMark("zooKeeper","zkProperties"));
        ObjectConstruct objectConstruct = new ObjectConstruct(project);
        return new ObjectMeta<>(ZkRegistryWrapper.class,objectConstruct,objectPropertyMarkList,null);
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
