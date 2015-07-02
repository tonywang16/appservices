package com.paimeilv.basic

import java.text.SimpleDateFormat

import com.paimeilv.DateUtils
import com.paimeilv.basic.Message
import com.paimeilv.basic.User

/** 消息发送 ***/
class MessageTo {
	
	Message msg //消息信息
	
	String type //消息类型
	
	Date	dateCreated //消息发送时间
	Date	lastUpdated 
	
	static belongsTo=[to:User,msg:Message]
	
	/** 发送时间设置 ***/
	public String getSendtime(){
		Date now = new Date()
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String,Long> map = DateUtils.comparison(now, dateCreated)
		
		Long dd = map.get("d") //天数
		Long hh = map.get("h") //小时
		Long mm = map.get("m") //分钟
		Long ss = map.get("s") //秒
		
		if(dd>=6){ //日期格式 yyyy-MM-dd  HH:mm:ss
			return format.format(dateCreated)
		}else if(dd>0){//星期几
			return dd+"天前"
		}else if(hh>0){ //几小时前
			return hh+"小时前"
		}else if(mm>0){//几分钟前
			return mm+"分钟前"
		}else if(ss>0){ //几秒前
			return ss+"秒前"
		}else{
			return "现在"
		}
	}
	
	/** 是否已读 默认为未读 */
	boolean isread=false

    static constraints = {
    }
}
