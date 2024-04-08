package com.backend.common.core;

public class CommonException extends RuntimeException {
    public CommonException() {
        super();
    }
    public CommonException(String message) {
        super(message);
    }
    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }
    public CommonException(Throwable cause) {
        super(cause);
    }
}
