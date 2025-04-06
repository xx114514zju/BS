package com.zju.mqtt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zju.mqtt.entity.Device;
import com.zju.mqtt.entity.Message;
import com.zju.mqtt.mapper.DeviceMapper;
import com.zju.mqtt.mapper.MessageMapper;
import kotlin.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin
public class DeviceController {
    @Autowired
    private DeviceMapper deviceMapper;
    @Autowired
    private MessageMapper messageMapper;

    @RequestMapping(value="/mountFirst", method = RequestMethod.POST)
    public List<Integer> mountFirst(HttpServletRequest request) {
        Integer id = (Integer) request.getAttribute("userId");
        if (id == null) {
            // 处理 id 为空的情况，比如返回错误信息或者默认数据
            return new ArrayList<>();
        }
        List<Integer> list = new ArrayList<>();
        int a = deviceMapper.findType1(id).size();
        int b = deviceMapper.findType2(id).size();
        int c = deviceMapper.findType3(id).size();
        int d = deviceMapper.findType4(id).size();
        int e = deviceMapper.findType5(id).size();
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        return list;
    }
    @RequestMapping(value = "/mountSecond", method = RequestMethod.POST)
    public  List<Integer> mountSecond(HttpServletRequest request)
    {
        Integer id = (Integer) request.getAttribute("userId");
        List<Integer> list = new ArrayList<>();
        int a = deviceMapper.find1Type1(id);
        int b = deviceMapper.find1Type2(id);
        int c = deviceMapper.find1Type3(id);
        int d = deviceMapper.find1Type4(id);
        int e = deviceMapper.find1Type5(id);
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(e);
        return list;
    }

    @RequestMapping(value = "/mountThird", method = RequestMethod.POST)
    public List<Integer> mountThird(HttpServletRequest request)
    {
        Integer id = (Integer) request.getAttribute("userId");
        List<Integer> list = new ArrayList<>();
        List<Integer> id_1 = deviceMapper.findType1(id); //id_1中包含了对应userid，类型为1的所有device的id
        int a1 = 0;
        for (Integer integer : id_1) {
            a1 += messageMapper.getMessage(integer).size();
        }
        list.add(a1);
        List<Integer> id_2 = deviceMapper.findType2(id); //id_1中包含了对应userid，类型为1的所有device的id
        int a2 = 0;
        for (Integer integer : id_2) {
            a2 += messageMapper.getMessage(integer).size();
        }
        list.add(a2);
        List<Integer> id_3 = deviceMapper.findType3(id); //id_1中包含了对应userid，类型为1的所有device的id
        int a3 = 0;
        for (Integer integer : id_3) {
            a3 += messageMapper.getMessage(integer).size();
        }
        list.add(a3);
        List<Integer> id_4 = deviceMapper.findType4(id); //id_1中包含了对应userid，类型为1的所有device的id
        int a4 = 0;
        for (Integer integer : id_4) {
            a4 += messageMapper.getMessage(integer).size();
        }
        list.add(a4);
        List<Integer> id_5 = deviceMapper.findType5(id); //id_1中包含了对应userid，类型为1的所有device的id
        int a5 = 0;
        for (Integer integer : id_5) {
            a5 += messageMapper.getMessage(integer).size();
        }
        list.add(a5);
        return list;
    }


    @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
    public List<Message> getMessage(HttpServletRequest request, @RequestParam String device_name, @RequestParam String device_id, @RequestParam String option, @RequestParam String order) {
        Integer id = (Integer) request.getAttribute("userId");
        List<Integer> id_list = deviceMapper.findIDDevice(id);
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("device_id", id_list);
        // 添加查询条件
        if (device_name != null && !device_name.isEmpty()) {
            queryWrapper.eq("device_name", device_name);
        }
        if (!Objects.equals(device_id, "0")) {
            queryWrapper.eq("device_id", device_id);
        }
        // 添加排序规则
        if (!Objects.equals(option, "0")) {
            switch (option) {
                case "1":
                    queryWrapper.eq("alert", 1);
                    break;
                case "2":
                    queryWrapper.eq("alert", 0);
                    break;
                default:
                    break;
            }
        }
        // 添加额外排序规则
        if (!Objects.equals(order, "0")) {
            switch (order) {
                case "1":
                    queryWrapper.orderByAsc("stamp");
                    break;
                case "2":
                    queryWrapper.orderByDesc("stamp");
                    break;
                default:
                    break;
            }
        }
        else
        {
            queryWrapper.orderByDesc("stamp");
        }
        // 执行查询
        return messageMapper.selectList(queryWrapper);
    }

    @RequestMapping(value = "/getPoints", method = RequestMethod.POST)
    public Pair<Integer,List<Message>> getMessage(HttpServletRequest request, @RequestParam Integer device_id)
    {
        Integer userid = (Integer) request.getAttribute("userId");
        int test = deviceMapper.match(device_id, userid);
        if(test == 0) return new Pair<>(0, null);
        List<Message> list = messageMapper.getTimeOrder(device_id);
        return new Pair<>(1, list);
    }

    @RequestMapping(value = "/getUserDevice", method = RequestMethod.POST)
    public List<Device> getMessage(HttpServletRequest request)
    {
        Integer id = (Integer) request.getAttribute("userId");
        return deviceMapper.findUserDevice(id);
    }

    @RequestMapping(value = "/editDevice", method = RequestMethod.POST)
    public Integer editDevice(HttpServletRequest request, @RequestParam Integer device_id, @RequestParam String device_type, @RequestParam String device_name, @RequestParam String device_desc)
    {
        Integer userid = (Integer) request.getAttribute("userId");
        //检查是否是正确的用户发来的请求
        int count = deviceMapper.match(device_id, userid);
        //没有对应的用户与设备id，就直接返回
        if(count <= 0) return -1;
        QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("name", device_name);
        if (deviceMapper.selectList(queryWrapper1).size()>0) return 0;
        UpdateWrapper<Device> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", device_id);
        if(!Objects.equals(device_type, "0")) updateWrapper.set("kind", device_type);
        if(!device_name.isEmpty()) updateWrapper.set("name", device_name);
        if(!device_desc.isEmpty()) updateWrapper.set("description", device_desc);
        int rowsAffected = deviceMapper.update(null, updateWrapper);
        if(rowsAffected>0) return 1;
        else return 0;
    }

    @RequestMapping(value = "/Adder", method = RequestMethod.POST)
    public Integer AddDevice(HttpServletRequest request, @RequestParam String device_name,@RequestParam String device_type,@RequestParam String device_des)
    {
        Integer user_id = (Integer) request.getAttribute("userId");
        QueryWrapper<Device> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("userid", user_id);
        queryWrapper1.eq("name", device_name);
        if (deviceMapper.selectList(queryWrapper1).size()>0) return 0;
        int a = deviceMapper.AddDevice(user_id, device_name, device_type, device_des, "notnow");
        if(a>0) return 1;
        return 0;
    }

    @RequestMapping(value = "/delDevice", method = RequestMethod.POST)
    public Integer delDevice(HttpServletRequest request, @RequestParam Integer device_id)
    {
        Integer user_id = (Integer) request.getAttribute("userId");
        int count = deviceMapper.match(device_id, user_id);
        //没有对应的用户与设备id，就直接返回
        if(count <= 0) return -1;
        int a = deviceMapper.delDevice(device_id);
        if(a > 0) return 1;
        return 0;
    }
}
