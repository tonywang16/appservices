package com.paimeilv.config

import com.paimeilv.basic.Circle
import com.paimeilv.basic.Image

/** 广告 **/
class Advert {

	String title
	String link
	Image image
	
	static belongsTo=[area:Circle]
	
    static constraints = {
		image nullable: true
		link unique: true
		title nullable: true
    }
}
