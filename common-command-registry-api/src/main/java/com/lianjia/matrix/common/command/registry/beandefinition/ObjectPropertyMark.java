package com.lianjia.matrix.common.command.registry.beandefinition;

/**
 * @author 程天亮
 * @Created
 */
public class ObjectPropertyMark {
    private String propertyName;

    private String propertyValueMarkName;

    public ObjectPropertyMark(String propertyName, String propertyValueMarkName) {
        this.propertyName = propertyName;
        this.propertyValueMarkName = propertyValueMarkName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValueMarkName() {
        return propertyValueMarkName;
    }
}