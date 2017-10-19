package com.lianjia.matrix.common.command.registry;


import com.lianjia.matrix.common.command.registry.exceptions.LifecycleException;

/**
 * Created by chengtianliang on 2016/10/20.
 */
public interface Lifecycleable {
    void init() throws LifecycleException;

    void destroy() throws LifecycleException;
}
