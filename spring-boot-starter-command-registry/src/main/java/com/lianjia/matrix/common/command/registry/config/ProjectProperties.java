package com.lianjia.matrix.common.command.registry.config;

import java.util.Map;

/**
 * @author 程天亮
 * @Created
 */
public class ProjectProperties {
    /**
     * Project的名字，通常为中文名
     */
    private String name;

    /**
     * Project Code，唯一标示
     */
    private String code;

    /**
     * Project Path，用来代表Project存储的上下文路径（可选）
     */
    private String path;

    /**
     * Project 存储的节点（可选）
     */
    private String node;

    private String address;

    private String appkey;

    private Map<String,Object> attributes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
}
