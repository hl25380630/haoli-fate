package com.haoli.fate.service;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.fate.dao.MailDao;
import com.haoli.sdk.web.domain.MailConfig;

@Service
public class MailService {
	
	@Autowired
	private MailDao mailDao;
	
	@Autowired
	UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(MailService.class);

	public MailConfig getConfig(Map<String, Object> params) {
		MailConfig mailConfig =  mailDao.getConfig(params);
		String pwd = mailConfig.getPassword();
		String decodePwd = new String(Base64.decodeBase64(pwd));
		mailConfig.setPassword(decodePwd);
		return mailConfig;
	}

}
