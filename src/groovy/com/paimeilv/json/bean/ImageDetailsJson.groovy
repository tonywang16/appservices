package com.paimeilv.json.bean

import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Image
import com.paimeilv.basic.Praise

class ImageDetailsJson {

	public ImageDetailsJson(Image img,com.paimeilv.basic.User u){
		if(!img) return
		this.imgId = img.id
		this.path = img.pathstr
		this.autor = img.user.fullname
		this.autorPhoto = img.user.photo
		this.time = img.sendtime
		this.favoNum = img.favorite?.size()?:0
		this.comNum=img.comment?.size()?:0
		this.autorIntroduction = img.user.gender+","+img.user.location
		if(u){
			Favorite f = Favorite.findWhere(image:img,user:u)
			if(f) isf =  true
			Praise p = Praise.findWhere(image:img,user:u)
			if(p) isp=  true
		}
	}
	
	Long imgId
	/** 图片路径 **/
	String path
	/** 发布者 **/
	String autor
	/** 发布者介绍 **/
	String autorIntroduction
	/** 发布者头像 **/
	String autorPhoto
	/** 发布时间 **/
	String time
	/** 收藏数 ***/
	Long favoNum
	/** 评论数 ***/
	Long comNum
	
	/** 是否收藏 ***/
	Boolean isf=false
	/** 是否点赞 ***/
	Boolean isp=false
}
