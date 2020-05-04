package com.interview.security.utils;

import com.interview.security.bean.JwtAuthenticationToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author kj
 * @Date 2020/4/29 9:56
 * @Version 1.0
 */
public class HttpHandlerUtils {

    private static SessionRegistry sessionStrategy = new SessionRegistryImpl();

    public static void successMessage(HttpServletResponse response){
        response.setStatus(HttpStatus.OK.value());
        //应该是返回一个成功的Message对象
    }

    /**
     * 将user对象转化为含有Username,role的token
     * @param user
     * @return
     */
    public static String parseAuthenticationToToken(User user) {
//        User user = (User) authentication.getPrincipal();

        String username = user.getUsername();
        StringBuffer role = new StringBuffer();

        for (Object o : user.getAuthorities().toArray()){
            role.append(o + ",");
        }

        role.replace(role.length() - 1, role.length(), "");

        return JwtUtils.createToken(username, role.toString());
    }

    public static String getCookeiValue(HttpServletRequest request, String key){
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            System.out.println("cookies为空");
            return null;
        }
        for (Cookie cookie : cookies){
            if(cookie.getName().equals(key)){
                return cookie.getValue();
            }
        }

        //不存在对应key的cookie
        return null;
    }

    public static Authentication getAuthentication(String username, String realToken){
        User userDetails = new User(username,
                realToken,
                true,
                true,
                true,
                true,
                AuthorityUtils.commaSeparatedStringToAuthorityList(JwtUtils.getUserRole(realToken)));
        return new JwtAuthenticationToken(userDetails, userDetails.getAuthorities());
    }
}
