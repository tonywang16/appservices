package com.paimeilv.basic

import java.util.Date;

/** 收藏 ****/
class Collect {
	
	Date dateCreated
	Date lastUpdated

	static belongsTo=[user:User,card:Card,point:Point,image:Image,article:Article]
    static constraints = {
		card nullable: true
		point nullable: true
		image nullable: true
		article nullable: true
    }
}
