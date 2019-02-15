package com.haoli.fate.api;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WechatApi {
	
	private Logger logger = LoggerFactory.getLogger(WechatApi.class);
	
	@GetMapping("/wechat/getMsg")
	public String getWechatMsg(HttpServletRequest request) throws Exception{
		String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        boolean flag = this.verifyUrl(signature, timestamp, nonce);
        if(flag) {
    		logger.info("success");
        	return echostr;
        }else {
    		return null;
        }
	}
	
	
	public boolean verifyUrl(String msgSignature, String timeStamp, String nonce)
	            throws Exception {
        // 这里的 WXPublicConstants.TOKEN 填写你自己设置的Token就可以了
        String signature = getSHA1("LihaoWechatServerToken", timeStamp, nonce);
        if (!signature.equals(msgSignature)) {
            throw new Exception("e");
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
