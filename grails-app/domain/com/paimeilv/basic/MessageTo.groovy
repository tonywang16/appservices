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
		return DateUtils.getSendTime(dateCreated)
	}
	
	/** 是否已读 默认为未读 */
	boolean isread=false

    static constraints = {
		type nullable: true
    }
}
