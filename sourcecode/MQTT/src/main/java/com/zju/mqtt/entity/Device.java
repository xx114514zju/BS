package com.zju.mqtt.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import javax.persistence.criteria.CriteriaBuilder;

@TableName("device")
public class Device {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String description;

    private Integer userid;

    private Integer kind;

    private String activate;


    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    // name 的 getter 方法
    public String getName() {
        return name;
    }

    // name 的 setter 方法
    public void setName(String name) {
        this.name = name;
    }

    // description 的 getter 方法
    public String getDescription() {
        return description;
    }

    // description 的 setter 方法
    public void setDescription(String description) {
        this.description = description;
    }

    // userid 的 getter 方法
    public Integer getUserid() {
        return userid;
    }

    // userid 的 setter 方法
    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    // kind 的 getter 方法
    public Integer getKind() {
        return kind;
    }

    // kind 的 setter 方法
    public void setKind(Integer kind) {
        this.kind = kind;
    }

    // activate 的 getter 方法
    public String getActivate() {
        return activate;
    }

    // kind 的 setter 方法
    public void setActivate(String activaye) {
        this.activate = activaye;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userid=" + userid +
                ", kind=" + kind +
                ", activate=" + activate +
                '}';
    }
}
