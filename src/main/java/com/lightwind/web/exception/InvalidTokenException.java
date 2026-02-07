package com.lightwind.web.exception;

/**
 * Token无效异常
 */
public class InvalidTokenException extends AuthenticationException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
