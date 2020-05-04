package com.interview.security.utils;

import com.interview.security.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kj
 * @Date 2020/4/26 16:06
 * @Version 1.0
 */
public class JwtUtils {
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String SUBJECT = "congge";
    /**
     * 有效时间
     */
    public static final long EXPIRITION = 1000 * 60 * 60 * 24;

    /**
     * 可用时间
     * 有效期-可用时间=刷新时间段
     */
    public static final long ENABLETIME = 1000 * 60;

    public static final String APPSECRET_KEY = "congge_secret";

    private static final String ROLE_CLAIMS = "rol";




    /**
     * 生成token
     * @param username
     * @param role
     * @return
     */
    public static String createToken(String username,String role,String password) {

        Map<String,Object> map = new HashMap<>(16);
        map.put(ROLE_CLAIMS, role);

        String token = Jwts
                .builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username",username)
                .claim("password", password)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    /**
     * 生成token
     * @param username
     * @param role
     * @return
     */
    public static String createToken(String username,String role) {

        Map<String,Object> map = new HashMap<>(16);
        map.put(ROLE_CLAIMS, role);

        String token = Jwts
                .builder()
                .setSubject(username)
                .setClaims(map)
                .claim("username",username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRITION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
        return token;
    }

    /**
     * 获取自定义参数列表
     * @param token
     * @return
     */
    public static Claims checkJWT(String token) {

        try {
            final Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取用户名
     * @param token
     * @return
     */
    public static String getUsername(String token) throws JwtAuthenticationException {
        Claims claims = null;
        try{
            claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        }catch (Exception e){
            throw new JwtAuthenticationException("用户解析异常");
        }
        return claims.get("username").toString();
    }
    /**
     * 获取密码
     * @param token
     * @return
     */
    public static String getPassword(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("password").toString();
    }

    /**
     * 获取用户角色
     * @param token
     * @return
     */
    public static String getUserRole(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.get("rol").toString();
    }

    /**
     * 是否过期
     * 返回为true为过期
     * false为不过期
     * @param token
     * @return
     */
    public static boolean isExpiration(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }

    /**
     * 当且仅当该token已过期时判断，过期token有没有超过可用时间
     * 已过可用时间返回需要刷新令牌,ture
     * 可用期，刷新期，有效期
     */

    public static boolean isRefreshToken(String token){
        Claims claims = Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody();
        //过期时间
        Date expiration = claims.getExpiration();
        //设置token时的时间
        long enableTime = expiration.getTime() - EXPIRITION + ENABLETIME;
        //判断当前时间是不是在可用期之后
        return new Date(enableTime).before(new Date());
    }

//    public static void main(String[] args) {
//        String name = "acong";
//        String role = "rol";
//        String password = "123";
//        String token = createToken(name,role,password);
//        System.out.println(token);
//
//        Claims claims = checkJWT(token);
//        System.out.println(claims.get("username"));
//
//        System.out.println(getUsername(token));
//        System.out.println(getUserRole(token));
//        System.out.println(getPassword(token));
//        System.out.println(isExpiration(token));
//
//    }
}
