package com.lianjia.matrix.common.command.registry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by chengtianliang on 2016/10/20.
 */
@Component("zkProperties")
public class ZkProperties {

    @Value("${registry.zk.connection-string:}")
    private String connectionString;

    @Value("${registry.zk.session-timeout-ms:2000}")
    private int sessionTimeoutMs;

    @Value("${registry.zk.connection-timeout-ms:2000}")
    private int connectionTimeoutMs;

    @Value("${registry.zk.max-close-wait-ms:500}")
    private int maxCloseWaitMs;

    /**
     * initial amount of time to wait between retries
     */
    @Value("${registry.zk.base-sleep-time-ms:0}")
    private int baseSleepTimeMs;

    @Value("${registry.zk.max-retries:3}")
    private int maxRetries;

    @Value("${registry.zk.max-sleep-ms:6000}")
    private int maxSleepMs = Integer.MAX_VALUE;

    @Value("${registry.zk.namespace:}")
    private String namespace = "";

    @Value("${registry.zk.username:}")
    private String username;

    @Value("${registry.zk.password:}")
    private String password;

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getMaxCloseWaitMs() {
        return maxCloseWaitMs;
    }

    public void setMaxCloseWaitMs(int maxCloseWaitMs) {
        this.maxCloseWaitMs = maxCloseWaitMs;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public int getMaxSleepMs() {
        return maxSleepMs;
    }

    public void setMaxSleepMs(int maxSleepMs) {
        this.maxSleepMs = maxSleepMs;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAuthority() {
        if ((username == null || username.length() == 0)
                && (password == null || password.length() == 0)) {
            return null;
        }
        return (username == null ? "" : username)
                + ":" + (password == null ? "" : password);
    }

    @Override
    public String toString() {
        return new StringBuilder(connectionString).append("@").append(getAuthority()).append(namespace).toString();
    }
}
