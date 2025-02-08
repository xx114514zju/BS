package com.zju.mqtt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.security.Timestamp;
import java.time.LocalDateTime;


@TableName("message")
public class Message {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Integer deviceId;
    private String deviceName;
    private Integer alert;
    private String info;
    private Double lat;
    private Double lng;
    private LocalDateTime stamp;
    private Integer value;

    // Constructors, getters, and setters

    public Message() {
    }

    public Message(Integer deviceId,String deviceName, Integer alert, String info, Double lat, Double lng, LocalDateTime stamp, Integer value) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.alert = alert;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.stamp = stamp;
        this.value = value;
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName(){return deviceName;}

    public void setDeviceName(String deviceName) {this.deviceName = deviceName;}

    public Integer getAlert() {
        return alert;
    }

    public void setAlert(Integer alert) {
        this.alert = alert;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public LocalDateTime getStamp() {
        return stamp;
    }

    public void setStamp(LocalDateTime stamp) {
        this.stamp = stamp;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    // toString() method

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", deviceId=" + deviceId +
                ", deviceName=" + deviceName +
                ", alert=" + alert +
                ", info='" + info + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", stamp=" + stamp +
                ", value=" + value +
                '}';
    }
}
