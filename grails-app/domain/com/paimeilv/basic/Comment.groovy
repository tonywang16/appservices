package com.paimeilv.basic

import com.paimeilv.DateUtils

/** 评论 */
class Comment {
	
	/** 评论 */
	String strText
	
	Date lastUpdated
	/** 发布设置 ***/
	public String getSendtime(){
		return DateUtils.getSendTime(lastUpdated)
	}
	
	static hasMany=[comment:Comment]
	
	static belongsTo=[parent:Comment,user:User,image:Image,article:Article]

    static constraints = {
		parent nullable: true
		image nullable: true
		article nullable: true
		
		strText(blank:false, nullable:false, size:0..500)
    }
}
