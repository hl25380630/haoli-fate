package com.haoli.fate.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.fate.domain.User;

@Mapper
public interface UserDao {

	User queryUser(Map<String, Object> params);

	Integer add(User user);

	User loadUserByPhone(String phone);

}
