package com.paimeilv.config

/**
 * 七牛配置表
 */
class QiniuConfig {

	String accesskey
	String secretkey
	String uploadbucket
	String tempbucket
	
	String tempurl
	
    static mapping = {
		version false
    }
    
	static constraints = {
    }
}
