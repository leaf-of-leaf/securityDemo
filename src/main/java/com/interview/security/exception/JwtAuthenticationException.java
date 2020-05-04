package com.interview.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author kj
 * @Date 2020/4/29 10:07
 * @Version 1.0
 * Jwt令牌认证错误异常
 */
public class JwtAuthenticationException extends AuthenticationException {

    public JwtAuthenticationException(String msg){
        super(msg);
    }

}
