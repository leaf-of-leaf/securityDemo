package com.interview.security.filter;

import com.interview.security.exception.ValidateCodeException;
import com.interview.security.utils.HttpHandlerUtils;
import com.interview.security.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author kj
 * @Date 2020/4/23 19:37
 * @Version 1.0
 */
@Component
public class ValidataCodeFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

//    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String loginUrl = "/login";
        String loginMethod = "POST";

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        if (loginUrl.equals(requestURI)
                && loginMethod.equals(method)) {
            try {
                ValidataCode(request);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void ValidataCode(HttpServletRequest request) throws ValidateCodeException {
        String codeInRequest = request.getParameter("ImageCode");
        String uuid = HttpHandlerUtils.getCookeiValue(request,"uuid");
        String codeInRedis = (String) JedisUtils.get(uuid);
        if (StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空！");
        }
        if (codeInRedis == null) {
            throw new ValidateCodeException("验证码已过期");
        }
        if (!codeInRedis.equals(codeInRequest)) {
            throw new ValidateCodeException("验证码不正确！");
        }
        //验证码正确但登录密码错误时，验证码要刷新才行
    }
}
