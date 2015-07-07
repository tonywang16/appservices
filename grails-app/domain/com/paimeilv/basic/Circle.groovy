package com.paimeilv.basic

import com.paimeilv.config.Advert


/** 圈子（区域） ***/
class Circle {

	/** 名称 **/
	String name
	
	String searchIndex
	
	public String getDescribe(){
		this.city.value+","+this.name
	}
	
	static belongsTo=[city:City]
	static hasMany=[point:Place,advert:Advert]
	
	static mapping = {
		version false
	}
	
    static constraints = {
		searchIndex nullable: true
    }
}
