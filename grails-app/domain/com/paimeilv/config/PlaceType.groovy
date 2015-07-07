package com.paimeilv.config

import com.paimeilv.basic.Place

/** 趣处类型 ****/
class PlaceType {
	
	String type
	
	static belongsTo=[place:Place]
	
	static mapping = {
		version false
	}
    static constraints = {
		
    }
}
