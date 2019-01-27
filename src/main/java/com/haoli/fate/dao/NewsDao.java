package com.haoli.fate.dao;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NewsDao {

	Integer batchAdd(Map<String, Object> map);

	Set<Long> getNewsIdList(Map<String, Object> params);

}
