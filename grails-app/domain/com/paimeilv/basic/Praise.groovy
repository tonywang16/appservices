package com.paimeilv.basic

import java.util.Date;

/** 点赞 ****/
class Praise {

	Date dateCreated
	Date lastUpdated
	static belongsTo=[user:User,card:Postcard,image:Image,article:Article]
    static constraints = {
		user nullable: true
		card nullable: true
		image nullable: true
		article nullable: true
    }
}
