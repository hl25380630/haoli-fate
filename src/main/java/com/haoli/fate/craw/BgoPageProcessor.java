package com.haoli.fate.craw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.haoli.sdk.web.util.MapUtil;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;


@Component
public class BgoPageProcessor implements PageProcessor{

    public static final String LIST_URL = "https://api\\.biligame\\.com/news/list.*";

    private Site site = Site.me().setSleepTime(3000);
    
    List<Map<String, Object>> newsList = new ArrayList<Map<String, Object>>();
    
    List<Map<String, Object>> newsDetailList = new ArrayList<Map<String, Object>>();
	
    @Override
    public void process(Page page) {
		if (page.getUrl().regex(LIST_URL).match()) {
			
			List<String> idList = new JsonPathSelector("$.data[*].id").selectList(page.getRawText());
			List<String> tempNewsList = new JsonPathSelector("$.data[*]").selectList(page.getRawText());
			for(String str : tempNewsList) {
				Map<String, Object> map = new HashMap<String, Object>();
				JSONObject jobj = JSONObject.parseObject(str);
				String id = String.valueOf(jobj.get("id"));
				String title = String.valueOf(jobj.get("title"));
				String createTime = String.valueOf(jobj.get("createTime"));
				map.put("id", id);
				map.put("title", title);
				map.put("createTime", createTime);
				newsList.add(map);
			}
	        if (CollectionUtils.isNotEmpty(idList)) {
	            for (String id : idList) {
	                page.addTargetRequest("https://api.biligame.com/news/" + id + ".action");
	            }
	        }
	        
		}
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			String content = new JsonPathSelector("$.data.content").select(page.getRawText());
			String id = new JsonPathSelector("$.data.id").select(page.getRawText());
			map.put("content", content);
			map.put("id", id);
			newsDetailList.add(map);
		}

    }

    @Override
    public Site getSite() {
        return site;
    }
    
    public List<Map<String, Object>> listNews() {
    	for(Map<String, Object> news : newsList) {
    		String id = MapUtil.getString(news, "id");
    		for(Map<String, Object> newsDetail : newsDetailList) {
    			String detailId = MapUtil.getString(newsDetail, "id");
    			if(id.equals(detailId)) {
    				String content = MapUtil.getString(newsDetail, "content");
    				news.put("content", content);
    			}
    		}
    	}
    	return newsList;
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
	
	public List<JSONObject> convert(List<String> list){
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
	
	

    public static void main(String[] args) {
    	BgoPageProcessor bp = new BgoPageProcessor();
        Spider.create(bp)
        .addUrl("https://api.biligame.com/news/list.action?gameExtensionId=45&positionId=2&pageNum=1&pageSize=1&typeId=")
        .run();
        List<Map<String, Object>> newsList = bp.listNews();
        System.out.println(newsList);
    }
}
