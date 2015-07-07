package com.paimeilv.basic


class City {
	String searchIndex
	String value
	Date lastUpdated

	static hasMany=[area:Circle]
	
	static mapping = {
		version false
	}
	
	public String getDescribe(){
		return value
	}

	static constraints = {
		searchIndex nullable: true
		value unique: true
	}
}
