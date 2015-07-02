package com.paimeilv.basic

class UserToken {
	
	/** 生成的令牌 ***/
	String accessToken
	
	/** 生成时间 ***/
	Date time
	
	/** 设备号 ***/
	String equipment
	
	static belongsTo=[user:User]
	
	static mapping = {
		version false
	}

    static constraints = {
    }
}
