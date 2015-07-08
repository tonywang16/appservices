package com.paimeilv.basic

import java.text.SimpleDateFormat;

import com.paimeilv.DateUtils

/** 对趣处进行评分 ***/
class Postcard {
	
	
	/** 评分 */
	Double  score
	
	/** 评论 */
	String comment
	
	Date	lastUpdated
	
	/** 发送时间设置 ***/
	public String getSendtime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(lastUpdated);
	}
	
	static belongsTo=[place:Place,user:User]
	static hasMany=[image:Image,praise:Praise,favorite:Favorite]
    static constraints = {
		comment nullable: true
    }
}
