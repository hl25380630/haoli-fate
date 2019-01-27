package com.haoli.fate.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.fate.domain.User;

@Mapper
public interface UserDao {

	User queryUser(Map<String, Object> params);

	Integer add(User user);

	List<User> listAll();

}
