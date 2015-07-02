package com.paimeilv.basic

/** 对趣处进行评分 ***/
class Postcard {
	
	
	/** 评分 */
	Double  score
	
	/** 评论 */
	String comment
	
	static belongsTo=[place:Place,user:User]
	static hasMany=[image:Image,praise:Praise,favorite:Favorite]
    static constraints = {
		comment nullable: true
    }
}
