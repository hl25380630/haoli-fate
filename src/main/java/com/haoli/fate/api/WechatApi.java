package com.haoli.fate.api;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.service.WechatService;

@RestController
public class WechatApi {
	
	@Autowired
	WechatService wechatService;
	
	@GetMapping("/wechat/authServcer")
	public String getWechatMsg(HttpServletRequest request) throws Exception{
		return wechatService.getWechatMsg(request);
	}

}
