package com.paimeilv.config

import com.paimeilv.DateUtils

class Captcha {
	
	String telphone
	
	String code
	
	Date lastUpdated
	
	static mapping = {
		version  false
	}

    static constraints = {
		
    }
}
