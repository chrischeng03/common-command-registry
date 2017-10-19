package com.lianjia.matrix.common.command.registry.support;

public class AuthToken {
    private String projectCode;

    private String appkey;

    public AuthToken(String projectCode, String appkey) {
        this.projectCode = projectCode;
        this.appkey = appkey;
    }

    public AuthToken() {
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
