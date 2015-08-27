package com.paimeilv.config

import com.paimeilv.DateUtils

class Log {

	/** 日志内容 ***/
	String value
	Date lastUpdated
	
	/** 发送时间设置 ***/
	public String getSendtime(){
		return DateUtils.getSendTime(lastUpdated)
	}
    static constraints = {
		value nullable:true
    }
}
