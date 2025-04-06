package com.zju.mqtt.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zju.mqtt.entity.Device;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceMapper extends BaseMapper<Device> {
    @Select("Select id from device where userid = #{ID} ")
    List<Integer> findIDDevice(@Param("ID") Integer ID);

    @Select("Select * from device where userid = #{ID} ")
    List<Device> findUserDevice(@Param("ID") Integer ID);

    @Select("Select id from device where userid = #{ID} and kind = 1")
    List<Integer> findType1(@Param("ID") Integer ID);

    @Select("Select id from device where userid = #{ID} and kind = 2")
    List<Integer> findType2(@Param("ID") Integer ID);

    @Select("Select id from device where userid = #{ID} and kind = 3")
    List<Integer> findType3(@Param("ID") Integer ID);

    @Select("Select id from device where userid = #{ID} and kind = 4")
    List<Integer> findType4(@Param("ID") Integer ID);

    @Select("Select id from device where userid = #{ID} and kind = 5")
    List<Integer> findType5(@Param("ID") Integer ID);

    @Select("Select count(*) from device where userid = #{ID} and kind = 1 and activate='now'")
    int find1Type1(@Param("ID") Integer ID);

    @Select("Select count(*) from device where userid = #{ID} and kind = 2 and activate='now'")
    int find1Type2(@Param("ID") Integer ID);

    @Select("Select count(*) from device where userid = #{ID} and kind = 3 and activate='now'")
    int find1Type3(@Param("ID") Integer ID);

    @Select("Select count(*) from device where userid = #{ID} and kind = 4 and activate='now'")
    int find1Type4(@Param("ID") Integer ID);

    @Select("Select count(*) from device where userid = #{ID} and kind = 5 and activate='now'")
    int find1Type5(@Param("ID") Integer ID);

    @Select("SELECT count(*) FROM device WHERE id = #{deviceid} AND userid = #{userid};")
    int match(@Param("deviceid") Integer device_id, @Param("userid") Integer userid);

    @Delete("DELETE FROM device WHERE id = #{deviceid};")
    int delDevice(@Param("deviceid") Integer device_id);

    @Insert("Insert into device(name, description, userid, kind, activate) values(#{name}, #{description}, #{userid}, #{kind}, #{activate});")
    int AddDevice(@Param("userid") Integer userId, @Param("name") String deviceName, @Param("kind") String deviceType,@Param("description") String deviceDes
    , @Param("activate") String activate);

    @Update("UPDATE device SET activate = 'now' where id = #{device_id};")
    void UpdateActivate(@Param("device_id") Integer device_id);


    @Update("UPDATE device SET activate = 'notnow' where id = #{device_id};")
    void UpdateUnActivate(@Param("device_id") Integer deviceId);

    @Select("select name from device where id = #{id};")
    String GetDeviceName(@Param("id") Integer ID);

    @Select("SELECT CASE WHEN activate = 'now' THEN true ELSE false END FROM device WHERE id = #{deviceId}")
    boolean getDeviceStatus(@Param("deviceId") Integer deviceId);
}
