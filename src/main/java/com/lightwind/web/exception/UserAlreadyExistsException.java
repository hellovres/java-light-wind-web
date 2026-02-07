package com.lightwind.web.exception;

/**
 * 用户已存在异常
 */
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
