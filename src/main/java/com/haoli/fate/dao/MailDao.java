package com.haoli.fate.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.haoli.sdk.web.domain.MailConfig;

@Mapper
public interface MailDao {

	MailConfig getConfig(Map<String, Object> params);


}
