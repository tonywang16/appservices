package com.paimeilv.basic

/** 消息本体 **/
class Message {
	
	String subject
	String content
	/** 消息来源：图片(img)地址 或者 文字(words)纯文字 ****/
	String come
	/** 消息类型：图片(img) 或者 文字(words) ****/
	String type 
	Date lastUpdated
	
	static belongsTo=[from:User]
	
	static hasMany=[messageTo:MessageTo]
	
    static constraints = {
		subject nullable: true
		come nullable: true
		type nullable: true
    }
}
