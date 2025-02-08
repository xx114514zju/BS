package com.zju.mqtt.service.serviceImpl;

import com.alibaba.fastjson2.JSONObject;
import com.zju.mqtt.entity.Message;
import com.zju.mqtt.mapper.DeviceMapper;
import com.zju.mqtt.mapper.MessageMapper;
import com.zju.mqtt.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    DeviceMapper deviceMapper;

    @Override
    public void messageHandler(MessageHeaders header, String payload) {
        String topic = String.valueOf(header.get(MqttHeaders.RECEIVED_TOPIC));
        if(topic == null) return;
        if(topic.startsWith("$SYS/brokers/") && (topic.endsWith("/connected") || topic.endsWith("/disconnected"))) {
            //设备上下线消息
            handleConnection(header, payload, topic);
        } else if(topic.length() > "/data".length() && topic.endsWith("/data")) {
            //设备数据消息
            handleMessage(header, payload, topic);
        } else return;
    }

    private void handleConnection(MessageHeaders header, String payload, String topic) {
        Integer device_id = Integer.valueOf(topic.split("/")[4]);
        if(topic.endsWith("/connected")) {
            deviceMapper.UpdateActivate(device_id);
        } else if(topic.endsWith("/disconnected")) {
            deviceMapper.UpdateUnActivate(device_id);
        }
    }

    private void handleMessage(MessageHeaders header, String payload, String topic) {
        Message message = new Message();
        Integer device_id = Integer.valueOf(topic.substring(1, topic.length() - "/data".length()));
        message.setDeviceId(device_id);
        message.setDeviceName(deviceMapper.GetDeviceName(device_id));
        try {
            long timestamp = Long.parseLong(String.valueOf(header.get("timestamp")));
            message.setStamp(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.ofHours(8)));
            JSONObject json = JSONObject.parseObject(payload);
            Double longitude = json.getDouble("longitude");
            Double latitude = json.getDouble("latitude");
            message.setLng(longitude);
            message.setLat(latitude);
            String info = json.getString("info");
            message.setInfo(info);
            Boolean alert = json.getBoolean("alert");
            if(alert) {
                message.setAlert(1);
            }
            else message.setAlert(0);
            message.setValue(new Random().nextInt());
            messageMapper.insert(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
