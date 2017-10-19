package com.lianjia.matrix.common.command.registry.beandefinition;

/**
 * @author 程天亮
 * @Created
 */
public class ObjectProperty {
    private String propertyName;

    private Object value;

    public ObjectProperty(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Object getValue() {
        return value;
    }
}
