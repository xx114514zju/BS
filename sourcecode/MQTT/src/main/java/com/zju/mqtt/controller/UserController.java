package com.zju.mqtt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zju.mqtt.entity.User;
import com.zju.mqtt.mapper.UserMapper;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {
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
    public int register(String username, String password, String email)
    {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .or()
                .eq("email", email);
        long i = userMapper.selectCount(queryWrapper);
        if(i > 0) return 0;
        userMapper.insert(user);
        return 1;
    }


    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public int reset(String username, String password, String email)
    {
        int count = userMapper.checkUsernameEmailMapping(username, email);
        if(count == 0) return 0; //账号邮箱不匹配
        userMapper.updatePasswordByUsernameAndEmail(username, password);
        return 1;
    }

    @RequestMapping(value = "/innerReset", method = RequestMethod.POST)
    public int innerReset(String username, String password)
    {
        userMapper.updatePasswordByUsernameAndEmail(username, password);
        return 1;
    }
    //使用邮箱或者账号都可以登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Pair<Integer, Pair<String, Integer>> login(String username, String password)
    {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password)
                .or()
                .eq("email", username)
                .eq("password", password);
        long count = userMapper.selectCount(queryWrapper);
        if(count>0)
        {
            if(userMapper.findUserrname(username)>0)  //通过用户名登录
            {
                int ID = userMapper.findID1(username);
                return new Pair<>(1, new Pair<>(username, ID));
            }
            else
            {
                String name = userMapper.findThroughEmail(username);
                int ID = userMapper.findID1(name);
                return new Pair<>(1, new Pair<>(name, ID));
            }
//            return new Pair<>(1, username);
//            else return  new Pair<>(2,userMapper.findThroughEmail(username));
        }
        else return new Pair<>(0,null);
    }

    @RequestMapping(value="/del", method = RequestMethod.POST)
    public String del(int id)
    {
        int i = userMapper.deleteById(id);
        if(i > 0) return "删除成功！";
        else return "删除失败！";
    }
    @RequestMapping(value="/resetUsername", method = RequestMethod.POST)
    public int resetUsername(String oldname, String newName)
    {
        int count = userMapper.findUserrname(newName);
        if(count > 0) return 0;
        else {
            userMapper.updateUsername(newName, oldname);
            return 1;
        }
    }

    @RequestMapping(value = "/resetemail", method = RequestMethod.POST)
    public int resetEmail(String oldname, String newemail)
    {
        int count = userMapper.findEmail(newemail);
        if(count > 0) return 0;
        else {
            userMapper.updateEmail(newemail, oldname);
            return 1;
        }
    }
}
