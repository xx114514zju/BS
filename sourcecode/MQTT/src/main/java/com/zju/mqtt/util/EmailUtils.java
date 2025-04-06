package com.zju.mqtt.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class EmailUtils {

    @Autowired
    private JavaMailSender javaMailSender;
    // 生成随机验证码
    public String generateVerificationCode() {
        Random random = new Random();
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            verificationCode.append(random.nextInt(10));
        }
        return verificationCode.toString();
    }

    // 发送验证码邮件
    public void sendVerificationCodeEmail(String toEmail, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1564549374@qq.com");
        message.setTo(toEmail);
        message.setSubject("密码重置验证码");
        message.setText("您的密码重置验证码是：" + verificationCode + "，请在10分钟内使用。");
        try {
            javaMailSender.send(message);
        } catch (org.springframework.mail.MailSendException e) {
            // 处理邮件发送异常
            System.err.println("邮件发送失败: " + e.getMessage());
        }
    }
}