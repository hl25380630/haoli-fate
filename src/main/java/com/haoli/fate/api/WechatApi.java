package com.haoli.fate.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.sdk.web.domain.JsonResponse;

@RestController
public class WechatApi {
	
	private Logger logger = LoggerFactory.getLogger(WechatApi.class);
	
	@PostMapping("/wechat/authServer")
	public JsonResponse<String> authServer(String data){
		logger.info(data);
		return null;
	}

}
