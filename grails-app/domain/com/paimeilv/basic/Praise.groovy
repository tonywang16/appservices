package com.paimeilv.basic

import java.util.Date;

/** 点赞 ****/
class Praise {

	Date dateCreated
	Date lastUpdated
	static belongsTo=[user:User,card:Card]
    static constraints = {
    }
}
