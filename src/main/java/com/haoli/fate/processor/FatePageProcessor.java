package com.haoli.fate.processor;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class FatePageProcessor implements PageProcessor{

    public static final String LIST_URL = "https://api\\.biligame\\.com/news/list\\.action?gameExtensionId=45&positionId=2.*";

    public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
    
    private Site site = Site.me().setDomain("game.bilibili.com")
    					.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36")
    					.setSleepTime(3000);

    @Override
    public void process(Page page) {
        //列表页
//    	List<String> urlList = new ArrayList<String>();
//    	page.addTargetRequests(urlList);
//        List<String> news = page.getHtml().xpath("//div[@class='news-core-list-text']"
//        											).all();
//      page.putField("news", news);
        page.putField("title", new JsonPathSelector("$.data[*].title").selectList(page.getRawText()));
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new FatePageProcessor())
        .addUrl("https://api.biligame.com/news/list.action?gameExtensionId=45&positionId=2&pageNum=1&pageSize=5&typeId=")
        .run();
    }
}
