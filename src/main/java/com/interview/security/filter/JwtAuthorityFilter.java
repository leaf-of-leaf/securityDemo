package com.interview.security.filter;

import com.interview.security.exception.JwtAuthenticationException;
import com.interview.security.utils.HttpHandlerUtils;
import com.interview.security.utils.JedisUtils;
import com.interview.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author kj
 * @Date 2020/4/28 19:03
 * @Version 1.0
 */
public class JwtAuthorityFilter extends BasicAuthenticationFilter {

    public JwtAuthorityFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }


    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {

        HttpHandlerUtils.successMessage(response);

    }


    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String authorization = request.getHeader(JwtUtils.TOKEN_HEADER);

        if(StringUtils.isEmpty(authorization)){
            chain.doFilter(request,response);
            return;
        }

        if(!authorization.startsWith(JwtUtils.TOKEN_PREFIX)){
            onUnsuccessfulAuthentication(request,response,new JwtAuthenticationException("客户端传来的令牌出现错误"));
            chain.doFilter(request,response);
            return;
        }

        String token = authorization.replaceAll(JwtUtils.TOKEN_PREFIX, "");

        String username = null;
        try{
            username = JwtUtils.getUsername(token);
        }
        //超过令牌有效期
        catch (ExpiredJwtException e) {
            System.out.println("已过期");
            chain.doFilter(request,response);
            return;
        }

        if(StringUtils.isEmpty(username)){
            onUnsuccessfulAuthentication(request,response,new JwtAuthenticationException("username为空"));
            chain.doFilter(request,response);
            return;
        }

        String realToken = (String) JedisUtils.get(username);

        if(StringUtils.isEmpty(realToken) || !realToken.equals(token)) {
            onUnsuccessfulAuthentication(request,response,new JwtAuthenticationException("token不正确"));
            chain.doFilter(request,response);
            return;
        }

        //已过可用期，但处于可刷新期
        if(JwtUtils.isRefreshToken(token)){
            System.out.println("token进行刷新");
            String refreshToken = JwtUtils.createToken(username, JwtUtils.getUserRole(realToken));
            JedisUtils.set(username,refreshToken);
            response.setHeader(JwtUtils.TOKEN_HEADER,JwtUtils.TOKEN_PREFIX + refreshToken);
            Authentication authentication = HttpHandlerUtils.getAuthentication(username, refreshToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response);
            return;
        }

        if(SecurityContextHolder.getContext().getAuthentication() == null){
            Authentication authentication = HttpHandlerUtils.getAuthentication(username, realToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            onSuccessfulAuthentication(request,response,authentication);
        }
        chain.doFilter(request,response);

    }
}
