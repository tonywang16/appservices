package com.paimeilv.json.bean


class AdvertJson {
	
	public AdvertJson(com.paimeilv.config.Advert a){
		if(!a) return
		this.aid = a.id
		this.title = a.title
		this.link = a.link
		this.image = a.image.pathstr
	}
	Long aid
	String title
	String link
	String image
}
