package com.interview.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author kj
 * @Date 2020/4/23 19:31
 * @Version 1.0
 */
public class ValidateCodeException extends AuthenticationException {
    private static final long serialVersionUID = 5022575393500654458L;

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
