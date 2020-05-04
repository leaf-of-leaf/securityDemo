package com.interview.security.handler;

import com.interview.security.utils.HttpHandlerUtils;
import com.interview.security.utils.JedisUtils;
import com.interview.security.utils.JwtUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author kj
 * @Date 2020/4/28 16:29
 * @Version 1.0
 */

@Component
public class SuccessAuthenticationHandler implements AuthenticationSuccessHandler {



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        response.setCharacterEncoding("UTF-8");
        User user = (User) authentication.getPrincipal();
        String token = HttpHandlerUtils.parseAuthenticationToToken(user);
        JedisUtils.set(JwtUtils.getUsername(token),token);
        response.setHeader(JwtUtils.TOKEN_HEADER, JwtUtils.TOKEN_PREFIX + token);
        PrintWriter writer = response.getWriter();
        writer.write("验证成功");
        writer.flush();
        writer.close();
    }
}
