package com.interview.security.controller;

import com.interview.security.bean.ImageCode;
import com.interview.security.utils.JedisUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

/**
 * @author kj
 * @Date 2020/4/22 21:17
 * @Version 1.0
 */
@RestController
//@Api(tags = "生成图形验证码接口")
public class ValidataController {

    public final static String SESSION_KEY_IMAGE_CODE = "SESSION_KEY_IMAGE_CODE";
    private final static Integer EXPIRETIME = 60;
//    public final static String SESSION_KEY_SMS_CODE = "SESSION_KEY_SMS_CODE";

//    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @GetMapping("/code/img")
    public void createImageCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));

        ImageCode imageCode = createImageCode();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        JedisUtils.setex(uuid, EXPIRETIME, imageCode.getCode());
        Cookie uCookie = new Cookie("uuid", uuid);
        uCookie.setPath("/");
        uCookie.setMaxAge(EXPIRETIME);
        response.addCookie(uCookie);
        ImageIO.write(imageCode.getImage(), "jpeg", response.getOutputStream());
        System.out.println(imageCode.getCode());
    }

    private ImageCode createImageCode() {

        int width = 100; // 验证码图片宽度
        int height = 36; // 验证码图片长度
        int length = 4; // 验证码位数

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < length; i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), EXPIRETIME);
    }

    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

//    @GetMapping("/code/sms")
//    public String createSmsCode(String mobile, HttpServletRequest request, HttpServletResponse response){
//
//        SmsCode smsCode = createSmsCode();
//        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_KEY_SMS_CODE + mobile, smsCode);
//        // 输出验证码到控制台代替短信发送服务
//        String result = "您的登录验证码为：" + smsCode.getCode() + "，有效时间为60秒";
//        System.out.println(result);
//        return result;
//    }

//    private SmsCode createSmsCode(){
//        StringBuffer stringBuffer = new StringBuffer();
//        Integer times = 6;
//        Random r = new Random();
//        for(int i = 0; i < times; i++){
//            stringBuffer.append(r.nextInt(10));
//        }
//
//        return new SmsCode(stringBuffer.toString(),60);
//    }


}
