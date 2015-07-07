package com.paimeilv.config

import com.paimeilv.basic.User

/** 意见反馈 ***/
class Feedback {

	String value
	
	static belongsTo=[user:User]
    static constraints = {
		value(blank:false, nullable:false, size:0..500)
    }
}
