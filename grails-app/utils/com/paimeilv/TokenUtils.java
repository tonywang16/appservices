package com.paimeilv;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

public class TokenUtils {
	
	public static boolean checkSignature(String timestamp,String signature,String nonce){
		String token ="962f6f9779fa80b";
		String[] arr = new String[]{timestamp,nonce,token};
		Arrays.sort(arr);
		String s ="";
		for (int i = 0; i < arr.length; i++) {
			s+=arr[i];
		}
		
		return signature.equals(getSha1(s));
	}
	
	public static String getSignature4Ticket(String timestamp,String noncestr,String ticket,String url){
		
//		String[] arr = new String[]{timestamp,noncestr,ticket,url};
		
		String s ="";
		s="jsapi_ticket="+ticket+"&noncestr="+noncestr+"&timestamp="+timestamp+"&url="+url;
//		Arrays.sort(arr);
//		String s ="";
//		for (int i = 0; i < arr.length; i++) {
//			s+=arr[i];
//		}
		
		return getSha1(s);
	}
	
	public static String getSha1(String str){
		if (null == str || 0 == str.length())
			return null;
		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("UTF-8"));
			byte[] md = mdTemp.digest();
			int j = md.length;
			char[] buf = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
	
	public static boolean  isAjax(HttpServletRequest request){
	    return  (request.getHeader("X-Requested-With") != null  && "XMLHttpRequest".equals( request.getHeader("X-Requested-With").toString())   ) ;
	}
	
	/**
	* 产生随机字符串
	* */
	public static final String randomString(int length) {
			Random randGen = null;
			char[] numbersAndLetters = null;
			if (length < 1) {
				return null;
			}
			if (randGen == null) {
				randGen = new Random();
				numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" +
				"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
				//numbersAndLetters = ("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
			}
			char [] randBuffer = new char[length];
			for (int i=0; i<randBuffer.length; i++) {
				randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
				//randBuffer[i] = numbersAndLetters[randGen.nextInt(35)];
			}
			return new String(randBuffer);
		}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}
}
