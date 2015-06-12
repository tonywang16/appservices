package com.paimeilv.basic

/** 评论 */
class Comment {
	
	/** 评论 */
	String strText
	
	static hasMany=[comment:Comment]
	
	static belongsTo=[parent:Comment,user:User,image:Image,article:Article]

    static constraints = {
		parent nullable: true
		image nullable: true
		article nullable: true
    }
}
