package com.zju.mqtt.Service;

import com.zju.mqtt.entity.Message;
import com.zju.mqtt.mapper.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@EnableScheduling
@Service
public class BatchMessageTask {
    private static final int BATCH_SIZE = 10000;

    @Autowired
    private MessageQueueService queueService;

    @Autowired
    private MessageMapper messageMapper;

    @Scheduled(fixedRate = 30000) // 每30s执行
    public void processBatch() {
        List<Message> messages = queueService.popMessages(BATCH_SIZE);
        System.out.println(messages.size());
        if (!messages.isEmpty()) {
            messageMapper.batchInsert(messages);
            queueService.removeProcessed(messages.size());
        }
    }
}