package com.zju.mqtt.Service;

import com.zju.mqtt.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageQueueService {
    private static final String MESSAGE_QUEUE_KEY = "mqtt:message:queue";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 入队操作（原handleMessage调用）
    public void pushMessage(Message message) {
        System.out.println("pushmessage: " + message);
        redisTemplate.opsForList().rightPush(MESSAGE_QUEUE_KEY, message);
    }

    // 批量出队（定时任务调用）
    public List<Message> popMessages(int batchSize) {
        // 使用类型明确的序列化器
        List<Object> rawMessages = redisTemplate.opsForList().range(MESSAGE_QUEUE_KEY, 0, batchSize - 1);
        if (rawMessages == null) return Collections.emptyList();

        return rawMessages.stream()
                .filter(obj -> obj instanceof Message)
                .map(obj -> (Message) obj)
                .collect(Collectors.toList());
    }

    // 删除已处理消息
    public void removeProcessed(int count) {
        redisTemplate.opsForList().trim(MESSAGE_QUEUE_KEY, count, -1);
    }
}