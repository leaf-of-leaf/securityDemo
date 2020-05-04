package com.interview.security.bean;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author kj
 * @Date 2020/4/22 20:48
 * @Version 1.0
 */
public class ImageCode {
    //图片，实质上是将图片数据放入缓冲区中
    private BufferedImage image;
    //验证码
    private String code;
    //过期时间
    private LocalDateTime expireTime;

    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = expireTime;
    }

    public ImageCode(BufferedImage image, String code, int expireTime) {
        this.image = image;
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireTime);
    }

    public Boolean isExpire(){
        return LocalDateTime.now().isAfter(this.expireTime);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        return "ImageCode{" +
                "image=" + image +
                ", code='" + code + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
