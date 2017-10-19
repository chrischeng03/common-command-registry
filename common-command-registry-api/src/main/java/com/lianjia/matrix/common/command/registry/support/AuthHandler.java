package com.lianjia.matrix.common.command.registry.support;

import com.lianjia.matrix.common.command.registry.entity.Project;
import com.lianjia.matrix.common.command.registry.exceptions.AuthErrorException;

public interface AuthHandler {

    /**
     * 授权
     *
     * @param project
     * @throws AuthErrorException
     */
    void auth(Project project) throws AuthErrorException;
}
