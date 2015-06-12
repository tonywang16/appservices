package com.paimeilv.basic

import com.paimeilv.config.Advert


/** 圈子（区域） ***/
class Area {

	/** 名称 **/
	String name
	
	String searchIndex
	
	static belongsTo=[city:City,advert:Advert]
	static hasMany=[point:Point]
	
	static mapping = {
		version false
	}
	
    static constraints = {
		searchIndex nullable: true
    }
}
