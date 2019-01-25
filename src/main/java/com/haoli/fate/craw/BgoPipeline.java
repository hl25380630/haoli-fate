package com.haoli.fate.craw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.haoli.fate.dao.NewsDao;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

@Component
public class BgoPipeline implements Pipeline{
	
	@Autowired
	NewsDao newsDao;

	@Override
	public void process(ResultItems resultItems, Task task) {
		List<JSONObject> newsList = this.convertToList(resultItems, "newsList");
		List<JSONObject> newsDetailList = this.convertToList(resultItems, "newsDetailList");
		for(JSONObject news : newsList) {
			String newsId = this.getValue("id", news);
			for(JSONObject newsDetail : newsDetailList) {
				String newsDetailId = this.getValue("id", newsDetail);
				if(newsId.equals(newsDetailId)) {
					String contentDetail = this.getValue("content", newsDetail);
					news.put("contentDetail", contentDetail);
				}
			}
		}
		System.out.println(newsList);
	}
	
	public String getValue(String valueKey, JSONObject item) {
		Object result = null;
		Set<Entry<String, Object>> entrySet = item.entrySet();
		for(Entry<String, Object> entry : entrySet) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if(valueKey.equals(key)) {
				result = value;
				break;
			}
		}
		return String.valueOf(result);
	}
	
	public List<JSONObject> convertToList(ResultItems resultItems, String key){
		List<String> list = resultItems.get(key);
		if(list == null) {
			return new ArrayList<JSONObject>();
		}
		List<JSONObject> result = new ArrayList<JSONObject>();
		for(String str : list) {
			JSONObject jobj = JSONObject.parseObject(str);
			result.add(jobj);
		}
		return result;
	}
}
