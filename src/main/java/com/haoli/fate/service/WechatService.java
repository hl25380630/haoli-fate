package com.haoli.fate.service;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class WechatService {
	
	
	@Value("${wechat.auth.server.token}")
	private String wechatAuthServerToken;
	
	private Logger logger = LoggerFactory.getLogger(WechatService.class);
	
	public String getWechatMsg(HttpServletRequest request) throws Exception {
		String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        boolean flag = this.verifyUrl(signature, timestamp, nonce);
        if(!flag) {
        	logger.info("验证服务器失败");
    		return null;
        }
        logger.info("成功验证服务器，返回给微信的字符串为：" + echostr);
        return echostr;
	}
	
	public boolean verifyUrl(String msgSignature, String timeStamp, String nonce)throws Exception {
		String signature = getSHA1(wechatAuthServerToken, timeStamp, nonce);
	    if (!signature.equals(msgSignature)) {
	        return false;
	    }
	    return true;
	}
	
	public String getSHA1(String token, String timestamp, String nonce) throws Exception {
        String[] array = new String[]{token, timestamp, nonce};
        StringBuffer sb = new StringBuffer();
        // 字符串排序
        Arrays.sort(array);
        for (int i = 0; i < 3; i++) {
            sb.append(array[i]);
        }
        String str = sb.toString();
        // SHA1签名生成
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes());
        byte[] digest = md.digest();

        StringBuffer hexstr = new StringBuffer();
        String shaHex = "";
        for (int i = 0; i < digest.length; i++) {
            shaHex = Integer.toHexString(digest[i] & 0xFF);
            if (shaHex.length() < 2) {
                hexstr.append(0);
            }
            hexstr.append(shaHex);
        }
        return hexstr.toString();
	}



}
