package com.zju.mqtt.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zju.mqtt.entity.User;
import com.zju.mqtt.mapper.UserMapper;
import com.zju.mqtt.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin
public class UserController {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserMapper userMapper;

    //http://localhost:8080/user
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    //直接返回json格式
    public List Query()
    {
        List<User> list = userMapper.selectList(null);
        return list;
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public int register(String username, String password, String email) {
        // 检查用户名或邮箱是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or()
                .eq("email", email);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return 0; // 用户名或邮箱已存在，注册失败
        }
        // 创建用户对象并设置属性
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        // 插入用户记录到数据库
        userMapper.insert(user);
        return 1; // 注册成功
    }

    //使用邮箱或者账号都可以登录
    //token completed
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestParam String username, @RequestParam String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).or().eq("email", username);
        User user = userMapper.selectOne(queryWrapper);
        Map<String, Object> response = new HashMap<>();
        if (user != null && user.getPassword().equals(password)) { // 直接比较哈希密码
            String token = jwtUtils.generateToken(user.getUsername(), user.getId());
            response.put("code", 1);
            response.put("name", username);
            response.put("id", user.getId());
            response.put("token", token);
        } else {
            response.put("code", 0);
            response.put("message", "Invalid username or password");
        }
        return response;
    }

    @RequestMapping(value="/del", method = RequestMethod.POST)
    public String del(int id)
    {
        int i = userMapper.deleteById(id);
        if(i > 0) return "删除成功！";
        else return "删除失败！";
    }

    @RequestMapping(value="/resetUsername", method = RequestMethod.POST)
    public int resetUsername(HttpServletRequest request, @RequestParam String newName)
    {
        int count = userMapper.findUserrname(newName);
        if(count > 0) return 0;
        else {
            Integer id = (Integer) request.getAttribute("userId");
            userMapper.updateUsername(newName, id);
            return 1;
        }
    }

    @RequestMapping(value = "/resetemail", method = RequestMethod.POST)
    public int resetEmail(HttpServletRequest request, @RequestParam String newemail)
    {
        int count = userMapper.findEmail(newemail);
        if(count > 0) return 0;
        else {
            Integer id = (Integer) request.getAttribute("userId");
            userMapper.updateEmail(newemail, id);
            return 1;
        }
    }
}
