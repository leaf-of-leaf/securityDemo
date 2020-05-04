package com.interview.security.filter;

import com.interview.security.bean.MyUser;
import com.interview.security.handler.FailureAuthenticationHandler;
import com.interview.security.handler.SuccessAuthenticationHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.session.NullAuthenticatedSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kj
 * @Date 2020/4/28 16:22
 * @Version 1.0
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        this.setSessionAuthenticationStrategy(new NullAuthenticatedSessionStrategy());
        setAuthenticationSuccessHandler(new SuccessAuthenticationHandler());
        setAuthenticationFailureHandler(new FailureAuthenticationHandler());
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        MyUser user = parseUser(request);
        System.out.println(request.getRequestURI());
        UsernamePasswordAuthenticationToken uToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());

        return this.authenticationManager.authenticate(uToken);
    }

    public static MyUser parseUser(HttpServletRequest request) {
        MyUser user = new MyUser();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username == null){
            username = "";
        }
        if(password == null){
            password = "";
        }
        user.setUserName(username);
        user.setPassword(password);
        return user;
    }
}
