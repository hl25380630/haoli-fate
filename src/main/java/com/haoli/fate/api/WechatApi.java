package com.haoli.fate.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.sdk.web.domain.JsonResponse;

@RestController
public class WechatApi {
	
	private Logger logger = LoggerFactory.getLogger(WechatApi.class);
	
	@GetMapping("/wechat/getMsg")
	public JsonResponse<String> getWechatMsg(String signature, String timestamp,
											String nonce, String echostr){
		logger.info("success");
		return null;
	}

}
