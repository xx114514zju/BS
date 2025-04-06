package com.zju.mqtt.controller;

import com.zju.mqtt.mapper.UserMapper;
import com.zju.mqtt.util.EmailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class EmailController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EmailUtils emailUtils;

    // 存储验证码及其有效期（这里简单使用Map，实际应用中建议使用缓存如Redis）
    private Map<String, String> verificationCodeMap = new HashMap<>();

    // 重置密码
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public int reset(String username, String password, String email, String verificationCode) {
        int count = userMapper.checkUsernameEmailMapping(username, email);
        if (count == 0) return 0; // 账号邮箱不匹配
        // 获取存储的验证码
        String storedVerificationCode = verificationCodeMap.get(email);
        if (storedVerificationCode == null ||!storedVerificationCode.equals(verificationCode)) {
            return -1; // 验证码不正确
        }
        // 验证通过，更新密码（此时的前端传来的密码已加密）
        userMapper.updatePasswordByUsernameAndEmail(username, password);
        // 密码更新成功后，删除验证码
        verificationCodeMap.remove(email);
        return 1;
    }

    // 发送验证码
    @RequestMapping(value = "/sendVerificationCode", method = RequestMethod.POST)
    public int sendVerificationCode(String username, String email) {
        int count = userMapper.checkUsernameEmailMapping(username, email);
        if (count == 0) return 0; // 账号邮箱不匹配

        String verificationCode = emailUtils.generateVerificationCode();
        emailUtils.sendVerificationCodeEmail(email, verificationCode);

        // 存储验证码并设置有效期（这里简单设置为10分钟，实际应用中根据需求调整）
        verificationCodeMap.put(email, verificationCode);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        verificationCodeMap.remove(email);
                    }
                },
                10 * 60 * 1000
        );
        return 1;
    }
}