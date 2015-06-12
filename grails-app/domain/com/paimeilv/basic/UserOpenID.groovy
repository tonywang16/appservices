package com.paimeilv.basic

import java.util.Date;

class UserOpenID {
	
	String openId 
	String accessToken
	String openType
	
	String openName //绑定方的昵称
	String openUrl //绑定方的头像地址
	
	Date dateCreated
	Date lastUpdated
	
	static belongsTo=[user:User]
	
    static constraints = {
		user nullable: true
		openName nullable: true
		openUrl nullable: true
    }
}
