package com.haoli.fate.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.fate.dao.UserDao;
import com.haoli.fate.domain.User;
import com.haoli.sdk.web.exception.ConditionException;
import com.haoli.sdk.web.util.Md5Util;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	RsaService rsaService;
	
	@Autowired
	TokenService tokenService;

	public void register(User user) throws Exception {
		String userName  =user.getUserName();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		User record = userDao.queryUser(params);
		if(record != null) {
			throw new ConditionException("用户已存在");
		}
		String pwd = rsaService.decrypt(user.getPassword());
		String salt = System.currentTimeMillis() + userName;
		user.setSalt(salt);
		String pwdMd5 = Md5Util.sign(pwd, salt, "UTF-8");
		user.setPassword(pwdMd5);
		user.setPhone("");
		user.setEmail("");
		user.setCreateTime(new Date());
		userDao.add(user);
	}

	public Map<String, Object> login(User user, String ip, String userAgent) throws Exception {
		User dbUser = loadUserByPhone(user.getPhone());
		if(dbUser == null) {
			throw new ConditionException("用户名或密码错误");
		}
		String pwd = rsaService.decrypt(user.getPassword());
		boolean check = Md5Util.verify(pwd, dbUser.getPassword(), dbUser.getSalt(), "UTF-8");
		if(!check) {
			throw new ConditionException("用户名或密码错误");
		}
		String token = tokenService.genToken(dbUser, ip, userAgent);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("token", token);
		result.put("phone", dbUser.getPhone());
		result.put("userName", dbUser.getUserName());
		return result;
	}
	
	public User loadUserByPhone(String phone) {
		User user = userDao.loadUserByPhone(phone);
		return user;
	}

}
