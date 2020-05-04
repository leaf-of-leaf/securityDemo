package com.interview.security.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ResourceBundle;

/**
 * 关于Jedis操作对象的工具类
 * @author kj
 * @Date 2020/3/17 15:59
 * @Version 1.0
 */
public class JedisUtils {

//    private static Properties properties = null;
    private static JedisPool jedisPool = null;
    private static JedisPoolConfig jedisPoolConfig = null;

    static {
//        properties = new Properties();
//        try{
//            properties.load(ClassLoader.getSystemResourceAsStream("redis.properties"));
//        } catch (Exception e){
//            e.printStackTrace();
//        }
        //ResourceBundle会查找 classpath 下的 .properties文件
        //获取其中内容，只需要读取文件名即可
        ResourceBundle resourceBundle = ResourceBundle.getBundle("redis");
        int maxTotal = Integer.parseInt(resourceBundle.getString("redis.pool.maxTotal"));
        int maxIdle = Integer.parseInt(resourceBundle.getString("redis.pool.maxIdle"));
        long maxWait = Long.parseLong(resourceBundle.getString("redis.pool.maxWait"));
        String ip = resourceBundle.getString("redis.ip");
        int port = Integer.parseInt(resourceBundle.getString("redis.port"));

        jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxTotal);

        jedisPool = new JedisPool(jedisPoolConfig,ip,port);
    }

    //保存set
    public static void set(Object key, Object value){
        Jedis jedis = getResource();
//        jedis.auth("kuangjie");
        jedis.set(SerializeUtils.serialize(key),
                SerializeUtils.serialize(value));
        jedis.close();
    }

    public static void setex(Object key, Integer seconds, Object value){
        Jedis jedis = getResource();
        jedis.setex(SerializeUtils.serialize(key),
                seconds,
                SerializeUtils.serialize(value));
        jedis.close();
    }

    //获取get
    public static Object get(Object key){
        Jedis jedis = getResource();
//        jedis.auth("kuangjie");
        byte[] bytes = jedis.get(SerializeUtils.serialize(key));
        jedis.close();
        if(bytes == null){
            System.out.println("redis中不存在此数据");
            return null;
        }
        return SerializeUtils.revSerialize(bytes);
    }

    //判断key存不存在
    public static boolean exists(Object key){
        Jedis jedis = getResource();
        jedis.auth("kuangjie");
        boolean flag = jedis.exists(SerializeUtils.serialize(key));
        jedis.close();
        return flag;
    }

    //删除del
    public static Object del(Object key){
        Jedis jedis = getResource();
        jedis.auth("kuangjie");
        long l = jedis.del(SerializeUtils.serialize(key));
        jedis.close();
        return l;
    }

    //获取长度getsize
    public static long getSize(){
        Jedis jedis = getResource();
        jedis.auth("kuangjie");
        long len = jedis.dbSize();
        jedis.close();
        return len;
    }

    //清空clear
    public static void clear(){
        Jedis jedis = getResource();
        jedis.auth("kuangjie");
        jedis.flushDB();
        jedis.close();
    }

    //获取连接对象Jedis
    public static Jedis getResource(){
        return jedisPool.getResource();
    }

    public static void main(String[] args) {

        JedisUtils.set("name","123");
        Object name = JedisUtils.get("name");
        System.out.println(name);
    }
}
