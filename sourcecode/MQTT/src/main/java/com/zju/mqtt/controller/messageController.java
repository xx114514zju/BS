package com.zju.mqtt.controller;

import com.zju.mqtt.Service.MessageQueueService;
import com.zju.mqtt.entity.Message;
import com.zju.mqtt.mapper.DeviceMapper;
import com.zju.mqtt.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin
public class messageController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DeviceMapper deviceMapper;
    @Autowired
    private MessageQueueService messageQueueService;

    @RequestMapping(value = "/mqtt", method = RequestMethod.POST)
    public String handleMqttRequest(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取参数
        Integer device_id = (Integer) requestBody.get("device_id");
        String option = (String) requestBody.get("option");
        Double longitude = (Double) requestBody.get("longitude");
        Double latitude = (Double) requestBody.get("latitude");
        String info = (String) requestBody.get("info");
        Boolean alert = (Boolean) requestBody.get("alert");

        System.out.println("device_id: " + device_id);
        System.out.println("option: " + option);
        System.out.println("longitude: " + longitude);
        System.out.println("latitude: " + latitude);
        System.out.println("info: " + info);
        System.out.println("alert: " + alert);

        if (device_id == null || option == null) {
            return "缺少必要参数: device_id 或 option";
        }

        // 生成设备状态缓存键
        String deviceStatusCacheKey = "device_status:" + device_id;
        // 尝试从 Redis 中获取设备状态
        Boolean isDeviceOnline = (Boolean) redisTemplate.opsForValue().get(deviceStatusCacheKey);
        if (isDeviceOnline == null) {
            // 缓存中不存在，从数据库中获取
            isDeviceOnline = deviceMapper.getDeviceStatus(device_id);
            // 将设备状态存入 Redis 缓存，设置缓存过期时间为 5 分钟
            redisTemplate.opsForValue().set(deviceStatusCacheKey, isDeviceOnline, 10, TimeUnit.MINUTES);
        }

        switch (option) {
            case "connected":
                if (isDeviceOnline) {
                    return "设备已上线，不能重复接收上线请求";
                }
                handleConnection(device_id, true);
                // 更新设备状态缓存
                redisTemplate.opsForValue().set(deviceStatusCacheKey, true, 10, TimeUnit.MINUTES);
                break;
            case "disconnected":
                if (!isDeviceOnline) {
                    return "设备未上线，不能接收下线请求";
                }
                handleConnection(device_id, false);
                // 更新设备状态缓存
                redisTemplate.opsForValue().set(deviceStatusCacheKey, false, 10, TimeUnit.MINUTES);
                break;
            case "normal":
                if (!isDeviceOnline) {
                    return "设备未上线，不能接收消息";
                }
                handleMessage(device_id, longitude, latitude, info, alert);
                break;
            default:
                return "无效的 option 参数值";
        }
        return "This is a response for /mqtt request";
    }


    private void handleConnection(Integer device_id, boolean isConnected) {
        if (isConnected) {
            deviceMapper.UpdateActivate(device_id);
        } else {
            deviceMapper.UpdateUnActivate(device_id);
        }
    }


    private static final Logger logger = LoggerFactory.getLogger(messageController.class);
    // 缓存过期时间，单位：秒

    @Async("asyncExecutor")
    public void handleMessage(Integer device_id, Double longitude, Double latitude,
                              String info, Boolean alert) {
        try {
            // 1. 构造消息对象（原有逻辑）
            Message message = new Message();
            message.setDeviceId(device_id);
            message.setStamp(LocalDateTime.now());
            message.setLng(longitude);
            message.setLat(latitude);
            message.setInfo(info);
            message.setAlert(alert ? 1 : 0);
            message.setValue(new Random().nextInt());

            // 2. 获取设备名称（带缓存逻辑）
            String deviceNameCacheKey = "device_name:" + device_id;
            String deviceName = (String) redisTemplate.opsForValue().get(deviceNameCacheKey);
            if (deviceName == null) {
                deviceName = deviceMapper.GetDeviceName(device_id);
                redisTemplate.opsForValue().set(deviceNameCacheKey,
                        deviceName != null ? deviceName : "NULL", 30, TimeUnit.SECONDS);
            }
            message.setDeviceName(deviceName);

            // 3. 推送到Redis队列（新增逻辑）
            messageQueueService.pushMessage(message);

            // 4. 日志记录（可选）
            logger.info("设备 {} 消息已加入队列，内容：{}", device_id, message);

        } catch (Exception e) {
            // 5. 异常处理（重要）
            logger.error("消息处理异常，设备ID：{}，错误信息：{}", device_id, e.getMessage());
        }
    }
//    @RequestMapping(value = "/mqtt", method = RequestMethod.POST)
//    public String handleMqttRequest(@RequestBody Map<String, Object> requestBody) {
//        // 从请求体中获取参数
//        Integer device_id = (Integer) requestBody.get("device_id");
//        String option = (String) requestBody.get("option");
//        Double longitude = (Double) requestBody.get("longitude");
//        Double latitude = (Double) requestBody.get("latitude");
//        String info = (String) requestBody.get("info");
//        Boolean alert = (Boolean) requestBody.get("alert");
//
//        System.out.println("device_id: " + device_id);
//        System.out.println("option: " + option);
//        System.out.println("longitude: " + longitude);
//        System.out.println("latitude: " + latitude);
//        System.out.println("info: " + info);
//        System.out.println("alert: " + alert);
//
//        if (device_id == null || option == null) {
//            return "缺少必要参数: device_id 或 option";
//        }
//
//        // 获取设备当前状态，假设 true 表示上线，false 表示未上线
//        boolean isDeviceOnline = deviceMapper.getDeviceStatus(device_id);
//
//        if ("connected".equals(option)) {
//            if (isDeviceOnline) {
//                return "设备已上线，不能重复接收上线请求";
//            }
//            handleConnection(device_id, true);
//        } else if ("disconnected".equals(option)) {
//            if (!isDeviceOnline) {
//                return "设备未上线，不能接收下线请求";
//            }
//            handleConnection(device_id, false);
//        } else if ("normal".equals(option)) {
//            if (!isDeviceOnline) {
//                return "设备未上线，不能接收消息";
//            }
//            handleMessage(device_id, longitude, latitude, info, alert);
//        } else {
//            return "无效的 option 参数值";
//        }
//
//        return "This is a response for /mqtt request";
//    }
//    private void handleMessage(Integer device_id, Double longitude, Double latitude, String info, Boolean alert) {
//        Message message = new Message();
//        message.setDeviceId(device_id);
//        message.setDeviceName(deviceMapper.GetDeviceName(device_id));
//        message.setStamp(LocalDateTime.now());
//        message.setLng(longitude);
//        message.setLat(latitude);
//        message.setInfo(info);
//        message.setAlert(alert ? 1 : 0);
//        message.setValue(new Random().nextInt());
//        messageMapper.insert(message);
//    }
}
