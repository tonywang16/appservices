package com.paimeilv.basic

/** 消息本体 **/
class Message {
	
	User from
	String subject
	String content
	Date lastUpdated
	
	static belongsTo=[from:User]
	
	static hasMany=[messageTo:MessageTo]
	
    static constraints = {
    }
}