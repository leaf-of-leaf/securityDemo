package com.interview.security.utils;

import java.io.*;

/**
 * 自定义序列化和反序列化工具类
 * @author kj
 * @Date 2020/3/17 14:41
 * @Version 1.0
 */

public class SerializeUtils {

    //序列化   java对象---->字节数组
    public static byte[] serialize(Object obj){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(oos != null){
                    oos.close();
                }
                if(baos != null){
                    baos.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return baos.toByteArray();
    }

    //反序列化
    public static Object revSerialize(byte[] bytes){

        ObjectInputStream ois = null;
        ByteArrayInputStream bais = null;

        Object obj = null;

        try{
            //将数组对象读入到流中
            bais = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);

            obj = ois.readObject();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try{
                if(ois != null){
                    ois.close();
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return obj;
    }

}
