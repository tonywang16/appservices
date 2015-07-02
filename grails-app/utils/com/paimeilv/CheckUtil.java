package com.paimeilv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	
	public static boolean checkTel(String tel){
		
		String regExp = "^[1]([3-9]{1})[0-9]{9}$";  
		Pattern p = Pattern.compile(regExp);  
		Matcher m = p.matcher(tel);  
		return m.find();//boolean
	}
	
	public static boolean checkEmail(String email){
		
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.find();//boolean
	}

}
