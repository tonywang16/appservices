package com.paimeilv.basic


class City {
	String searchIndex
	String value
	Date lastUpdated

	static hasMany=[area:Circle]
	
	static mapping = {
		version false
	}

	static constraints = {
		searchIndex nullable: true
		value unique: true
	}
}
