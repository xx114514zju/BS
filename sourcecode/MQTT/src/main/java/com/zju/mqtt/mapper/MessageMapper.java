package com.zju.mqtt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zju.mqtt.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Select("select * from message where device_id = #{id} ORDER BY stamp DESC;") //0000
    List<Message> getMessage(@Param("id") Integer id);



    @Select("SELECT * FROM message WHERE device_id = #{id} ORDER BY stamp DESC LIMIT 10;")
    List<Message> getTimeOrder(@Param("id") Integer id);

    void batchInsert(List<Message> messages);
}
