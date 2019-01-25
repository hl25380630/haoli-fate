package com.haoli.fate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.haoli.fate.craw.BgoPageProcessor;

import us.codecraft.webmagic.Spider;

@Service
public class DataService {
	
	@Autowired
	BgoPageProcessor bgoProcessor;
	
	@Async
	public void getBgoNews() {
		Spider.create(bgoProcessor).addUrl("https://api.biligame.com/news/list.action?gameExtensionId=45&positionId=2&pageNum=1&pageSize=1&typeId=").run();
	}

}
