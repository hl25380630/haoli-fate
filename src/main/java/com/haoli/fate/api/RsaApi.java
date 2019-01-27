package com.haoli.fate.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.service.RsaService;
import com.haoli.sdk.web.domain.JsonResponse;

@RestController
public class RsaApi {
	
	@Autowired
	RsaService rsaService;
	
	@GetMapping("/rsa/encrypt")
	public JsonResponse<String> encrypt(String text) throws Exception{
		String s = rsaService.encrypt(text);
		return new JsonResponse<String>(s);
	}
	
	@GetMapping("/rsa/decrypt")
	public JsonResponse<String> decrypt(String text) throws Exception{
		String s = rsaService.decrypt(text);
		return new JsonResponse<String>(s);
	}

}
