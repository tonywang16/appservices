package com.paimeilv.config

import com.paimeilv.basic.Area
import com.paimeilv.basic.Image

/** 广告 **/
class Advert {

	String title
	String link
	Image image
	
	static belongsTo=[area:Area]
	
    static constraints = {
		image nullable: true
		link unique: true
		title nullable: true
    }
}
