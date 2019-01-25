package com.haoli.fate.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.service.DataService;
import com.haoli.sdk.web.domain.JsonResponse;

@RestController
public class DataApi {
	
	@Autowired
	DataService dataService;

	@GetMapping("/data/bgo/news/get")
	public JsonResponse<String> getBgoNews(){
		dataService.getBgoNews();
		return JsonResponse.success();
	}
	
}
