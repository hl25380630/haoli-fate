package com.haoli.fate.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.haoli.fate.constant.NewsConstant;
import com.haoli.fate.craw.BgoPageProcessor;
import com.haoli.fate.dao.NewsDao;
import com.haoli.fate.domain.BgoNews;
import com.haoli.fate.domain.User;
import com.haoli.sdk.web.domain.MailConfig;
import com.haoli.sdk.web.domain.PageResult;
import com.haoli.sdk.web.util.EmailUtil;
import com.haoli.sdk.web.util.MapUtil;

import us.codecraft.webmagic.Spider;

@Service
public class DataService {
	
	@Autowired
	NewsDao newsDao;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	UserService userService;
	
	@Value("${bgo.news.url}")
	private String bgoNewsUrl;
	
	private Logger logger = LoggerFactory.getLogger(DataService.class);
	
	
	@Transactional(propagation=Propagation.REQUIRED, rollbackFor={Exception.class, RuntimeException.class})
	public void getBgoNews(Map<String, Object> params) throws Exception {
		Integer pageSize = MapUtil.getInteger(params, "pageSize");
		Integer pageNo = MapUtil.getInteger(params, "pageNo");
		BgoPageProcessor bgoProcessor = new BgoPageProcessor();
		String url = bgoNewsUrl + "&pageNum=" + pageNo +"&pageSize=" + pageSize;
		Spider.create(bgoProcessor).addUrl(url).run();
		List<Map<String, Object>> newsList = bgoProcessor.listNews();
		Map<String, Object> qparams = new HashMap<String, Object>();
		qparams.put("type", NewsConstant.NEWS_TYPE_BGO);
		Set<Long> recordedBgoNewsIdSet = this.getNewsIdList(qparams);
		List<Map<String, Object>> addList = new ArrayList<Map<String, Object>>();
		for(Map<String, Object> news : newsList) {
			Long newsId = MapUtil.getLong(news, "newsId");
			if(!recordedBgoNewsIdSet.contains(newsId)) {
				news.put("type", NewsConstant.NEWS_TYPE_BGO);
				addList.add(news);
			}
		}
		if(addList.size() > 0 ) {
			//添加新闻到数据库
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("newsList", addList);
			newsDao.batchAdd(map);
			//发送通知
			Map<String, Object> mailParams = new HashMap<String, Object>();
			mailParams.put("userId", 3L);
			MailConfig mailConfig = mailService.getConfig(mailParams);
			EmailUtil mailSupport = new EmailUtil(mailConfig);
			String content = "b站有新的fgo公告发布啦";
			String subject = "bgo新公告";
			List<User> userList = userService.listAll();
			String[] toList = new String[userList.size()];
			for(int i=0; i<userList.size(); i++) {
				toList[i] = userList.get(i).getEmail();
			}
			String emailAddr ="lihao_100@boe.com.cn";
			String cc = null;
			String[] sendAttachList = null;
			String[] contentIds = null;
			mailSupport.sendEmail(toList, subject, content, cc, contentIds, sendAttachList);
			logger.info("send mail success, to->{}, subject->{}", new Object[] {emailAddr, subject});
		}
	}
	
	public Set<Long> getNewsIdList(Map<String, Object> params){
		return newsDao.getNewsIdList(params);
	}

	public PageResult<BgoNews> pageListBgoNews(Map<String, Object> params) {
		Integer pageSize = MapUtil.getInteger(params, "pageSize");
		Integer pageNo = MapUtil.getInteger(params, "pageNo");
		Integer start = (pageNo-1)*pageSize;
		Integer limit = pageSize;
		params.put("start", start);
		params.put("limit", limit);
		params.put("type", NewsConstant.NEWS_TYPE_BGO);
		Integer total = newsDao.pageCountBgoNews(params);
		if(total == 0) {
			return new PageResult<BgoNews>(0, new ArrayList<BgoNews>());
		}
		List<BgoNews> bgoNewsList = newsDao.pageListBgoNews(params);
		return new PageResult<BgoNews>(total, bgoNewsList);
	}

	public BgoNews getBgoNewsDetail(Long id) {
		BgoNews bgoNews = newsDao.getBgoNewsDetail(id);
		return bgoNews;
	}

}
