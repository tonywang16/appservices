package com.paimeilv.json.bean

import com.paimeilv.basic.Image

class ImageJson {
	
	public ImageJson(Image img){
		if(!img) return
		this.imgId = img.id
		this.path = img.pathstr
		this.autor = img.user.fullname
		this.autorPhoto = img.user.photo
		this.time = img.sendtime
		this.favoNum = img.favorite?.size()?:0
		this.comNum=img.comment?.size()?:0
		
	}
	
	Long imgId 
	/** 图片路径 **/
	String path
	/** 发布者 **/
	String autor
	/** 发布者头像 **/
	String autorPhoto
	/** 发布时间 **/
	String time
	/** 收藏数 ***/
	Long favoNum
	/** 评论数 ***/
	Long comNum
}
