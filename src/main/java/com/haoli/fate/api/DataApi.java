package com.haoli.fate.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.domain.BgoNews;
import com.haoli.fate.service.DataService;
import com.haoli.sdk.web.domain.JsonResponse;
import com.haoli.sdk.web.domain.PageResult;

@RestController
public class DataApi {
	
	@Autowired
	DataService dataService;

	@PostMapping("/data/bgo/news/get")
	public JsonResponse<String> getBgoNews(@RequestBody Map<String, Object> params) throws Exception{
		dataService.getBgoNews(params);
		return JsonResponse.success();
	}
	
	@PostMapping("/data/bgo/news/pageList")
	public JsonResponse<PageResult<BgoNews>> pageListBgoNews(@RequestBody Map<String, Object> params){
		PageResult<BgoNews> result = dataService.pageListBgoNews(params);
		return new JsonResponse<PageResult<BgoNews>>(result);
	}
	
}
