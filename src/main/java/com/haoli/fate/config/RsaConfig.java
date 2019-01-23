package com.haoli.fate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONObject;
import com.haoli.sdk.web.domain.RSAKey;
import com.haoli.sdk.web.util.FileUtil;

@Configuration
public class RsaConfig {
	
	@Value("${rsa.path}")
	private String rsaKeyPath;
	
	@Bean
	public RSAKey getRsaKey() throws Exception {
		String s = FileUtil.readFile(rsaKeyPath);
		RSAKey keys = new RSAKey();
		JSONObject jobj = JSONObject.parseObject(s);
		String privateKey = jobj.getString("RsaPrivateKey");
		String publicKey = jobj.getString("RsaPublicKey");
		keys.setPrivateKeyString(privateKey);
		keys.setPublicKeyString(publicKey);
		return keys;
	}

}
