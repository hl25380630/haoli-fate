package com.haoli.fate.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.service.DataService;
import com.haoli.sdk.web.domain.JsonResponse;

@RestController
public class DataApi {
	
	@Autowired
	DataService dataService;

	@PostMapping("/data/bgo/news/get")
	public JsonResponse<String> getBgoNews(@RequestBody Map<String, Object> params) throws Exception{
		dataService.getBgoNews(params);
		return JsonResponse.success();
	}
	
}
