package com.paimeilv.basic

import java.text.SimpleDateFormat;
import java.util.Date;

import com.paimeilv.DateUtils

/** 对趣处进行评分 ***/
class Postcard {
	
	
	/** 评分 */
	Double  score
	
	/** 评论 */
	String comment
	
	Date	lastUpdated
	
	Date time
	
	/** 发送时间设置 ***/
	public String getSendtime(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(time){
			return format.format(time);
		}else{
			return format.format(lastUpdated);
		}
	}
	
	static belongsTo=[place:Place,user:User]
	static hasMany=[image:Image,praise:Praise,favorite:Favorite]
    static constraints = {
		time nullable: true
		comment nullable: true
    }
}
