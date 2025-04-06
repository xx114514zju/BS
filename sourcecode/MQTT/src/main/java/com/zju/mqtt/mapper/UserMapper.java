package com.zju.mqtt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zju.mqtt.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND id = #{id}")
    int match(@Param("username") String username, @Param("id") Integer id);

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username} AND email = #{email}")
    int checkUsernameEmailMapping(@Param("username") String username, @Param("email") String email);

    @Update("UPDATE user SET password = #{newPassword} WHERE username = #{username}")
    void updatePasswordByUsernameAndEmail(
            @Param("username") String username,
            @Param("newPassword") String newPassword
    );

    @Select("SELECT COUNT(*) FROM user WHERE username = #{username}")
    int findUserrname(@Param("username") String username);


    @Update("UPDATE user SET username = #{newUsername} WHERE id = #{id}")
    void updateUsername(@Param("newUsername") String newname, @Param("id") Integer Id);

    @Select("SELECT COUNT(*) FROM user WHERE email = #{username}")
    int findEmail(@Param("username") String email);

    @Update("UPDATE user SET email = #{newemail} WHERE id = #{id}")
    void updateEmail(@Param("newemail") String newemail, @Param("id") Integer Id);

    @Select("SELECT username FROM user WHERE email = #{username}")
    String findThroughEmail(@Param("username") String email);

    @Select("SELECT id from user WHERE username = #{username}")
    int findID1(@Param("username") String username);

}

