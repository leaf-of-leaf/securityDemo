package com.interview.security.exception;

import javax.naming.AuthenticationException;

/**
 * @author kj
 * @Date 2020/4/29 10:36
 * @Version 1.0
 * Jwt令牌解析异常
 */
public class JwtParseException extends AuthenticationException {
    public JwtParseException(String explanation) {
        super(explanation);
    }
}
