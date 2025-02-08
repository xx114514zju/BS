package com.zju.mqtt.Configer;

import com.zju.mqtt.service.MessageService;
import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.event.MqttConnectionFailedEvent;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.*;

@Data
@Configuration
@IntegrationComponentScan
public class MqttConfig {

    @Autowired
    MessageService messageService;

    @Value("${mqtt.hostUrl}")
    private String hostUrl;
    @Value("guest")
    private String username;
    @Value("123456")
    private String password;
    @Value("${mqtt.keepAliveInterval}")
    private int keepAliveInterval;
    @Value("${mqtt.connectionTimeout}")
    private int connectionTimeout;
    @Value("${mqtt.client-id}")
    private String clientId;
    @Value("${mqtt.server-id}")
    private String serverId;
    @Value("${mqtt.data-topic}")
    private String dataTopic;
    @Value("${mqtt.will-topic}")
    private String willTopic;
    @Value("${mqtt.will-content}")
    private String willContent;
    @Value("${mqtt.completion-timeout}")
    private long completionTimeout;

    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(hostUrl.split(","));
        options.setKeepAliveInterval(keepAliveInterval);
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        options.setConnectionTimeout(connectionTimeout);
        // 设置“遗嘱”消息的话题，若客户端与服务器之间的连接意外中断，服务器将发布客户端的“遗嘱”消息。
        options.setWill(willTopic,(clientId + " " + willContent).getBytes(),1,true);
        // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
        // 把配置里的 cleanSession 设为false，客户端掉线后 服务器端不会清除session，
        // 当重连后可以接收之前订阅主题的消息。当客户端上线后会接受到它离线的这段时间的消息
        options.setCleanSession(true);
        options.setMaxInflight(1000);

        //设置断开重新连接
        options.setAutomaticReconnect(true);
        return options;
    }

    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory(){
        DefaultMqttPahoClientFactory clientFactory = new DefaultMqttPahoClientFactory();
        clientFactory.setConnectionOptions(getMqttConnectOptions());
        return clientFactory;
    }



    @Bean
    @ServiceActivator(inputChannel = "mqttOutBoundChannel")
    public MessageHandler messageHandler(){
        MqttPahoMessageHandler mqttPahoMessageHandler = new MqttPahoMessageHandler(clientId,mqttPahoClientFactory());
        //async如果为true，则调用方不会阻塞。而是在发送消息时等待传递确认。默认值为false（发送将阻塞，直到确认发送)
        mqttPahoMessageHandler.setAsync(true);
        mqttPahoMessageHandler.setDefaultTopic(dataTopic);
        mqttPahoMessageHandler.setDefaultQos(1);
        mqttPahoMessageHandler.setAsyncEvents(true);
        return mqttPahoMessageHandler;
    }

    @Bean
    public MessageChannel mqttOutBoundChannel() { return new DirectChannel(); }

    @Bean
    public MessageChannel mqttInboundChannel(){return new DirectChannel();}

    @Bean
    public MessageProducer inBound(){
        String[] topics = new String[]{
                "$SYS/brokers/+/clients/+/disconnected", //客户端下线主题
                //第一个通配符表示服务器名，第二个表示设备id
                "$SYS/brokers/+/clients/+/connected",    //客户端上线主题
                dataTopic,
                willTopic
        };
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(serverId,mqttPahoClientFactory(),topics);
        adapter.setOutputChannel(mqttInboundChannel());
        adapter.setDisconnectCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public MessageHandler getInbound(){
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String payload = String.valueOf(message.getPayload());
                MessageHeaders header = message.getHeaders();
                //消息处理
                messageService.messageHandler(header, payload);
                System.out.println("消息内容："+payload+",header: "+ header);
            }
        };
    }

}