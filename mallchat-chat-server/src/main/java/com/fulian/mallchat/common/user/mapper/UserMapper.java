package com.fulian.mallchat.common.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fulian.mallchat.common.user.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author fulian
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-10-28 21:06:28
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




