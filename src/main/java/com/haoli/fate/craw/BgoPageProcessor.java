package com.haoli.fate.craw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;


@Component
public class BgoPageProcessor implements PageProcessor{

    public static final String LIST_URL = "https://api\\.biligame\\.com/news/list.*";

    private Site site = Site.me().setSleepTime(3000);
    
    List<String> newsList = new ArrayList<String>();
    
    List<String> newsDetailList = new ArrayList<String>();
	
    @Override
    public void process(Page page) {
		if (page.getUrl().regex(LIST_URL).match()) {
			page.putField("title", new JsonPathSelector("$.data[*].title").selectList(page.getRawText()));
			List<String> idList = new JsonPathSelector("$.data[*].id").selectList(page.getRawText());
			newsList = new JsonPathSelector("$.data[*]").selectList(page.getRawText());
			page.putField("newsList", newsList);
	        if (CollectionUtils.isNotEmpty(idList)) {
	            for (String id : idList) {
	                page.addTargetRequest("https://api.biligame.com/news/" + id + ".action");
	            }
	        }
		}
		else {
			String newsDetail = new JsonPathSelector("$.data[*]").select(page.getRawText());
	        newsDetailList.add(newsDetail);
	        page.putField("newsDetailList", newsDetailList);
		}

    }

    @Override
    public Site getSite() {
        return site;
    }
    
	public List<JSONObject> getResult() {
		List<JSONObject> convertedNewsList = this.convert(newsList);
		List<JSONObject> convertedNewsDetailList = this.convert(newsDetailList);
		for(JSONObject news : convertedNewsList) {
			String newsId = this.getValue("id", news);
			for(JSONObject newsDetail : convertedNewsDetailList) {
				String newsDetailId = this.getValue("id", newsDetail);
				if(newsId.equals(newsDetailId)) {
					String contentDetail = this.getValue("content", newsDetail);
					news.put("contentDetail", contentDetail);
				}
			}
		}
		return convertedNewsList;
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
        .addUrl("https://api.biligame.com/news/list.action?gameExtensionId=45&positionId=2&pageNum=1&pageSize=5&typeId=")
        .run();
        System.out.println("final result: " + bp.getResult());
    }
}
