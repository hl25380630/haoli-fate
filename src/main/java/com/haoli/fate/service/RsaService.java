package com.haoli.fate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.sdk.web.domain.RSAKey;
import com.haoli.sdk.web.util.RsaUtil;

@Service
public class RsaService {
	
	@Autowired
	RSAKey RsaKeySet;
	
	public String decrypt(String text) throws Exception {
		String source = RsaUtil.decrypt(text, RsaKeySet.getPrivateKeyString());
		return source;
	}
	
	public String encrypt(String source) throws Exception {
		String text = RsaUtil.encrypt(source, RsaKeySet.getPublicKeyString());
		return text;
	}

}
