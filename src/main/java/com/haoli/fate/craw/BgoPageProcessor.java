package com.haoli.fate.craw;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;


@Component
public class BgoPageProcessor implements PageProcessor{

    public static final String LIST_URL = "https://api\\.biligame\\.com/news/list.*";

    private Site site = Site.me().setSleepTime(3000);
    
	List<String> ids = new ArrayList<String>();
	
	List<String> titleList = new ArrayList<String>();
	
	List<String> contentList = new ArrayList<String>();

    @Override
    public void process(Page page) {
		if (page.getUrl().regex(LIST_URL).match()) {
			page.putField("title", new JsonPathSelector("$.data[*].title").selectList(page.getRawText()));
			ids = new JsonPathSelector("$.data[*].id").selectList(page.getRawText());
			titleList = new JsonPathSelector("$.data[*].title").selectList(page.getRawText());
			page.putField("titleList", titleList);
	        if (CollectionUtils.isNotEmpty(ids)) {
	            for (String id : ids) {
	                page.addTargetRequest("https://api.biligame.com/news/" + id + ".action");
	            }
	        }
		}
		else {
	        String content = new JsonPathSelector("$.data.content").select(page.getRawText());
	        contentList.add(content);
	        page.putField("content", content);
		}

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
    	BgoPageProcessor bp = new BgoPageProcessor();
        Spider.create(bp)
        .addUrl("https://api.biligame.com/news/list.action?gameExtensionId=45&positionId=2&pageNum=1&pageSize=1&typeId=")
        .run();
    }
}
