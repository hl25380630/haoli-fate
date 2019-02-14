package com.haoli.fate.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.haoli.fate.service.DataService;

@Component
public class BgoNewsTask {
	
	@Autowired
	DataService dataService;
	
	/**
	 * 每天每隔8小时更新一次bgo新闻
	 * @throws Exception 
	 */
	@Scheduled(cron = "0 0 0/6 * * ? ")
	public void getBgoNews() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("pageSize", 5);
		params.put("pageNo", 1);
		dataService.getBgoNews(params);
	}

}
