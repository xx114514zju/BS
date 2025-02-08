package com.zju.mqtt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zju.mqtt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND email = #{email}")
    int checkUsernameEmailMapping(@Param("username") String username, @Param("email") String email);

    @Update("UPDATE user SET password = #{newPassword} WHERE username = #{username}")
    void updatePasswordByUsernameAndEmail(
            @Param("username") String username,
            @Param("newPassword") String newPassword
    );

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int findUserrname(@Param("username") String username);


    @Update("UPDATE user SET username = #{newUsername} WHERE username = #{oldUsername}")
    void updateUsername(@Param("newUsername") String newname, @Param("oldUsername") String oldname);

    @Select("SELECT COUNT(*) FROM user WHERE email = #{username}")
    int findEmail(@Param("username") String email);

    @Update("UPDATE user SET email = #{newemail} WHERE username = #{oldname}")
    void updateEmail(@Param("newemail") String newemail, @Param("oldname") String oldname);

    @Select("SELECT username FROM user WHERE email = #{username}")
    String findThroughEmail(@Param("username") String email);

    @Select("SELECT id from user WHERE username = #{username}")
    int findID1(@Param("username") String username);

}

