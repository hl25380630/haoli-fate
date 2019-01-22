package com.haoli.fate.api;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.domain.User;
import com.haoli.fate.service.UserService;
import com.haoli.sdk.web.domain.JsonResponse;
import com.haoli.sdk.web.util.IpUtil;


@RestController
public class UserApi {

	@Autowired
	private UserService userService;
	
	@PostMapping("/user/register")
	public JsonResponse<String> register(@RequestBody User user) throws Exception {
		 userService.register(user);
		 return JsonResponse.success();
	}
	
	@PostMapping("/user/login")
	public JsonResponse<Map<String, Object>> login(HttpServletRequest httpRequest, @RequestHeader(required=true, name="user-agent")String userAgent, @RequestBody User userInfo) throws Exception {
		String ip = IpUtil.getIP(httpRequest);
		Map<String, Object> result = userService.login(userInfo, ip, userAgent);
		return new JsonResponse<Map<String, Object>>(result);
	}
}
