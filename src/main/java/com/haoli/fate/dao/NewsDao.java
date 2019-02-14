package com.haoli.fate.dao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.fate.domain.BgoNews;

@Mapper
public interface NewsDao {

	Integer batchAdd(Map<String, Object> map);

	Set<Long> getNewsIdList(Map<String, Object> params);

	Integer pageCountBgoNews(Map<String, Object> params);

	List<BgoNews> pageListBgoNews(Map<String, Object> params);

	BgoNews getBgoNewsDetail(Long id);

}
