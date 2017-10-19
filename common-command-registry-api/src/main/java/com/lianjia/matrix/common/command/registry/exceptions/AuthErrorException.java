package com.lianjia.matrix.common.command.registry.exceptions;

public class AuthErrorException extends RuntimeException {

    private int errorCode;

    private String msg;

    public AuthErrorException(int errorCode, String msg) {
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public AuthErrorException(Throwable cause, int errorCode, String msg) {
        super(cause);
        this.errorCode = errorCode;
        this.msg = msg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getMsg() {
        return msg;
    }
}
