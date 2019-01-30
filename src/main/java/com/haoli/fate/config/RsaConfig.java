package com.haoli.fate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.haoli.sdk.web.domain.RSAKey;
import com.haoli.sdk.web.util.FileUtil;

@Configuration
public class RsaConfig {
	
	@Value("${rsa.privateKey}")
	private String RsaPrivateKey;
	
	@Value("${rsa.publicKey}")
	private String RsaPublicKey;
	
	@Bean
	public RSAKey getRsaKey() throws Exception {
		RSAKey keys = new RSAKey();
		keys.setPrivateKeyString(RsaPrivateKey);
		keys.setPublicKeyString(RsaPublicKey);
		return keys;
	}

}
