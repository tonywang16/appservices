package com.paimeilv.json.bean

/** 趣处 ***/
class Place {
	
	public Place(com.paimeilv.basic.Place p){
		if(!p) return
		this.pid=p.id
		this.name=p.name
		this.takeIndex=p.takeIndex
		this.address=p.address
		this.cover=p.cover
		this.longitude=p.longitude
		this.latitude=p.latitude
	}
	
	Long pid

	/** 名称 **/
	String name
	
	/** 拍摄指数 */
	Double  takeIndex
	
	/** 地址 **/
	String address
	
	/** 封面 ****/
	String cover
	
	/** 经度 ***/
	String longitude
	
	/** 纬度 ***/
	String latitude
	
}
