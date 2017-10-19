package com.lianjia.matrix.common.command.registry.exceptions;

/**
 * Created by chengtianliang on 2016/10/20.
 */
public class LifecycleException extends RuntimeException {
    public LifecycleException() {
    }

    public LifecycleException(String message) {
        super(message);
    }

    public LifecycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public LifecycleException(Throwable cause) {
        super(cause);
    }

    public LifecycleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
