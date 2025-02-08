package com.zju.mqtt.service;

import org.springframework.messaging.MessageHeaders;

public interface MessageService {
    void messageHandler(MessageHeaders header, String payload);
}
