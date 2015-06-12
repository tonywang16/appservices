package com.paimeilv.basic

/** 对趣处进行评分 ***/
class Card {
	
	
	/** 评分 */
	Double  score
	
	/** 评论 */
	String comment
	
	static belongsTo=[point:Point,user:User]
	static hasMany=[image:Image,praise:Praise,collect:Collect]
    static constraints = {
		comment nullable: true
    }
}
