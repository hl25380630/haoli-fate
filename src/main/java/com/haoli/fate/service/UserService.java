package com.haoli.fate.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.fate.dao.UserDao;
import com.haoli.fate.domain.User;
import com.haoli.sdk.web.domain.RSAKey;
import com.haoli.sdk.web.exception.ConditionException;
import com.haoli.sdk.web.util.Md5Util;
import com.haoli.sdk.web.util.RsaUtil;

@Service
public class UserService {
	
	@Autowired
	RSAKey rasKeySet;
	
	@Autowired
	UserDao userDao;

	public void register(User user) throws Exception {
		String userName  =user.getUserName();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userName", userName);
		User record = userDao.queryUser(params);
		if(record != null) {
			throw new ConditionException("用户已存在");
		}
		String privateKey = rasKeySet.getPrivateKeyString();
		String pwd = RsaUtil.decrypt(user.getPassword(),privateKey);
		String salt = System.currentTimeMillis() + userName;
		user.setSalt(salt);
		String pwdMd5 = Md5Util.sign(pwd, salt, "UTF-8");
		user.setPassword(pwdMd5);
		user.setPhone("");
		user.setEmail("");
		user.setCreateTime(new Date());
		userDao.add(user);
	}

}
