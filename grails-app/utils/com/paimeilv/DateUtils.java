package com.paimeilv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class DateUtils {
	
	
	public static void main(String[] args) throws ParseException {
		
		Date now = new Date();
		String dateStop = "2015-01-23 15:54:30";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Date dateCreated = format.parse(dateStop);
		SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
		
		Map<String,Long> map = DateUtils.comparison(now, dateCreated);
		
		Long dd = map.get("d"); //天数
		Long hh = map.get("h");//小时
		Long mm = map.get("m"); //分钟
		Long ss = map.get("s"); //秒
		System.out.println(map);
		
		if(dd>=6){ //日期格式 yyyy-MM-dd  HH:mm:ss
			System.out.println(format.format(dateCreated));
		}else if(dd>0){//星期几
			System.out.println( dd+"天前");
		}else if(hh>0){ //几小时前
			System.out.println( hh+"小时前");
		}else if(mm>0){//几分钟前
			System.out.println( mm+"分钟前");
		}else if(ss>0){ //几秒前
			System.out.println( ss+"秒前");
		}else{
			System.out.println("现在");
		}
	}
	
	public static String getSendTime(Date lastUpdated){
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String,Long> map = DateUtils.comparison(now, lastUpdated);
		
		Long dd = map.get("d"); //天数
		Long hh = map.get("h"); //小时
		Long mm = map.get("m"); //分钟
		Long ss = map.get("s"); //秒
		
		if(dd>=6){ //日期格式 yyyy-MM-dd  HH:mm:ss
			return format.format(lastUpdated);
		}else if(dd>0){//星期几
			return dd+"天前";
		}else if(hh>0){ //几小时前
			return hh+"小时前";
		}else if(mm>0){//几分钟前
			return mm+"分钟前";
		}else if(ss>0){ //几秒前
			return ss+"秒前";
		}else{
			return "现在";
		}
	}
	
	/**
	 * 两时间比较 返回相差的时间数（包括：天数，小时数、分钟数、秒数）
	 * **/
	public static Map<String,Long> comparison(Date now,Date old){
			long diff = now.getTime() - old.getTime();
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			
			Map<String,Long> map = new HashMap<String,Long>();
			map.put("d",diffDays);
			map.put("h",diffHours);
			map.put("m",diffMinutes);
			map.put("s",diffSeconds);
			
			return map;
	}
	
	public static boolean comparison2b(Date now,Date old){
		long diff = now.getTime() - old.getTime();
		
		long diffDays = diff / (24 * 60 * 60 * 1000);
		if(diffDays>=1){
			return true;
		}
		return false;
	}
}
