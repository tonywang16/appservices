package com.paimeilv.json.bean

/** 地点 ***/
class Site {
	
	public Site(Long sid,String name,String describe,String type){
		this.sid = sid
		this.name = name
		this.describe = describe
		this.type = type
	}
	
	/** 地点ID ***/
	Long sid 

	/** 地点名称 ***/
	String name
	
	/** 地点描述 ***/
	String describe
	
	/** 地点类型 city:城市,circle:圈子,place:趣处 ***/
	String type
}
