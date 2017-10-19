package com.lianjia.matrix.common.command.registry.support;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.AuthErrorException;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

public class DefaultAuthHandler implements AuthHandler {

    private List<AuthToken> authTokens = new ArrayList<>();

    public void setAuthTokens(List<AuthToken> authTokens) {
        if (null != authTokens) {
            this.authTokens.addAll(authTokens);
        }
    }

    @Override
    public void auth(Project project) throws AuthErrorException {
        AuthToken authToken = findAuthToken(project.getCode());
        if (authToken == null) throw new AuthErrorException(404, "No AuthToken found");
        String key = project.getAddress() + project.getCode();
        String md5 = DigestUtils.md5Hex(key + authToken.getAppkey());
        if (!project.getAuthCode().equals(md5)) {
            throw new AuthErrorException(500, "Auth Error");
        }
    }

    private AuthToken findAuthToken(String projectCode) {
        for (AuthToken authToken : authTokens) {
            if (projectCode.equals(authToken.getProjectCode())) {
                return authToken;
            }
        }
        return null;
    }
}
