package com.lianjia.matrix.common.command.registry.beandefinition;

import java.util.List;

/**
 * @author 程天亮
 * @Created
 */
public class ObjectMeta<T> {

    private List<ObjectPropertyMark> propertyMarks;

    private List<ObjectProperty> properties;

    private ObjectConstruct objectConstruct;

    private Class<? extends T> targetClass;

    public ObjectMeta(Class<? extends T> targetClass, ObjectConstruct objectConstruct, List<ObjectPropertyMark> propertyMarks,List<ObjectProperty> properties) {
        this.propertyMarks = propertyMarks;
        this.properties = properties;
        this.objectConstruct = objectConstruct;
        this.targetClass = targetClass;
    }
    public List<ObjectPropertyMark> getPropertyMarks() {
        return propertyMarks;
    }

    public List<ObjectProperty> getProperties() {
        return properties;
    }

    public ObjectConstruct getObjectConstruct() {
        return objectConstruct;
    }

    public Class<? extends T> getTargetClass() {
        return targetClass;
    }
}
