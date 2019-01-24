package com.haoli.fate.craw;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.haoli.fate.dao.BgoDao;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class BgoPipeline implements Pipeline{
	
	@Autowired
	BgoDao bgoDao;

	@Override
	public void process(ResultItems resultItems, Task task) {
		JSONArray array = JSONObject.parseArray(String.valueOf(resultItems.get("titleList")));
		List<String> titleList = array.toJavaList(String.class);
		
	}
	
	
}
