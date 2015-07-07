package com.paimeilv.basic

import java.text.SimpleDateFormat
import java.util.Date;

import com.paimeilv.DateUtils

/** 文章 ***/
class Article {

	/**标题**/
	String title
	
	/** 类型 **/
	String type 
	
	/** 内容 **/
	String content
	
	Date	lastUpdated
	
	/** 发送时间设置 ***/
	public String getSendtime(){
		return DateUtils.getSendTime(lastUpdated)
	}
	
	static hasMany=[favorite:Favorite,comment:Comment,image:Image]
	static belongsTo=[user:User]
	
    static constraints = {
    }
}
