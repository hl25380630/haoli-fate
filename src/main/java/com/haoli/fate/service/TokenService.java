package com.haoli.fate.service;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.haoli.fate.domain.User;
import com.haoli.sdk.web.exception.ConditionException;


@Service
public class TokenService {
	
	@Autowired
	RsaService rasService;
	
	private Logger logger = LoggerFactory.getLogger(TokenService.class);
	
	public String genToken(User user, String ip, String userAgent) throws Exception {
		Client client = buildClient(userAgent);
		StringBuilder source = new StringBuilder(String.valueOf(user.getId()));
		source.append("^");
		source.append(ip);
		source.append("^");
		source.append(client.getClient());
		source.append("^");
		source.append(client.getOs());
		source.append("^");
		source.append(client.getModel());
		source.append("^");
		source.append(String.valueOf(System.currentTimeMillis()));
		byte[] bytes = Base64.encodeBase64(source.toString().getBytes("UTF-8"));
		String text = new String(bytes, "UTF-8");
		return rasService.encrypt(text);
	}
	
	public Long checkToken(String token, String ip, String userAgent) throws Exception {
		Client client = buildClient(userAgent);
		String source = null;
		try{
			source = rasService.decrypt(token);
		} catch(Exception e) {
			logger.error("checkToken error", e);
			return null;
		}
		byte[] bytes = Base64.decodeBase64(source.getBytes("UTF-8"));
		String text = new String(bytes, "UTF-8");
		String[] array = text.split("\\^");
		String userId = array[0];
		String sourceIp = array[1];
		String sourceClient = array[2];
		String sourceOs = array[3];
		String sourceModel = array[4];
		if(!sourceIp.equals(ip)) {
			throw new ConditionException("非法请求");
		}
		if(!sourceClient.equals(client.getClient()) || !sourceOs.equals(client.getOs()) || !sourceModel.equals(client.getModel())) {
			throw new ConditionException("非法请求");
		}
		return Long.valueOf(userId);
	}
	
	private class Client {
		
		private String client;
		
		private String os;
		
		private String model;
		
		public Client(String client, String os, String model){
			this.client = client;
			this.os = os;
			this.model = model;
		}

		public String getClient() {
			return client;
		}

		public String getOs() {
			return os;
		}

		public String getModel() {
			return model;
		}
		
	}
	
	private Client buildClient(String agent){
		String client = "c";
		String os = "o";
		String model = "m";
		try {
			String[] userAgent = agent.substring(agent.indexOf("(") + 1, agent.indexOf(")")).split(";");
			int leg = userAgent.length;
			if (leg >= 4) {
				if (userAgent[2].indexOf("Android") != -1) {
					client = "Android";
					os = userAgent[2];
					model = userAgent[3];
				} else {
					client = userAgent[0];
					os = userAgent[1];
					model = userAgent[2];
				}
			} else if (leg >= 3) {
				client = userAgent[0];
				os = userAgent[1];
			} else if (leg == 1) {
				client = userAgent[0];
			}
		} catch (Exception exception) {
		}
		return new Client(client, os, model);
	}

}
