package com.paimeilv.config

import java.util.Date;

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
