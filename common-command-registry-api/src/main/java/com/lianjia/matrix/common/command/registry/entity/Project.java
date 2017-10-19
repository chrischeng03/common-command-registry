package com.lianjia.matrix.common.command.registry.entity;

import com.lianjia.matrix.common.command.registry.utils.NetUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chengtianliang
 *
 * 项目实体，每个接入方代表一个Project
 */
public class Project {
    /**
     * Project的名字，通常为中文名
     */
    private String name;

    /**
     * Project Code，唯一标示
     */
    private String code;

    /**
     * 授权码，可选
     */
    private String authCode;

    /**
     * Project Path，用来代表Project存储的上下文路径（可选）
     */
    private String path;

    /**
     * Project 存储的节点（可选）
     */
    private String node;

    private String address;

    private Map<String, Object> attributes = new HashMap<>();

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

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNode() {
        if (node != null) {
            return node;
        }
        StringBuilder sb = new StringBuilder();
        if (address != null) {
            sb.append(address);
        }
        if (path != null) {
            if (!path.startsWith("/")) {
                sb.append("/");
            }
            sb.append(path);
        }
        node = sb.toString();
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getAddress() {
        if (address == null) {
            address = NetUtils.getV4Ip();
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public void setAttributes(Map<String, Object> attributes) {
        if (null != attributes) {
            this.attributes.putAll(attributes);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (code != null ? !code.equals(project.code) : project.code != null) return false;
        if (path != null ? !path.equals(project.path) : project.path != null) return false;
        if (node != null ? !node.equals(project.node) : project.node != null) return false;
        return address != null ? address.equals(project.address) : project.address == null;
    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (node != null ? node.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        return result;
    }
}
