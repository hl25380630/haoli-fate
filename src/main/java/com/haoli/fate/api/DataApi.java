package com.haoli.fate.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.haoli.fate.domain.BgoNews;
import com.haoli.fate.service.DataService;
import com.haoli.sdk.web.domain.JsonResponse;
import com.haoli.sdk.web.domain.PageResult;

@RestController
public class DataApi {
	
	@Autowired
	DataService dataService;

	/**
	 * 获取bgo最新新闻
	 */
	@PostMapping("/data/bgo/news/get")
	public JsonResponse<String> getBgoNews(@RequestBody Map<String, Object> params) throws Exception{
		dataService.getBgoNews(params);
		return JsonResponse.success();
	}
	
	/**
	 *分页显示bgo新闻列表 
	 */
	@PostMapping("/data/bgo/news/pageList")
	public JsonResponse<PageResult<BgoNews>> pageListBgoNews(@RequestBody Map<String, Object> params){
		PageResult<BgoNews> result = dataService.pageListBgoNews(params);
		return new JsonResponse<PageResult<BgoNews>>(result);
	}
	
	/**
	 * 获取bgo新闻详情
	 */
	@GetMapping("/data/bgo/news/detail")
	public JsonResponse<BgoNews> getBgoNewsDetail(@RequestParam Long id){
		BgoNews bgoNews = dataService.getBgoNewsDetail(id);
		return new JsonResponse<BgoNews>(bgoNews);
	}
	
}
