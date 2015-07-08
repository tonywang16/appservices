package com.paimeilv.json.bean

import com.paimeilv.basic.Comment

class CommentJson {
	
	public CommentJson(Comment c){
		if(!c) return
		this.cid = c.id
		this.content = c.strText
		this.autor = c.user.fullname
		this.autorPhoto = c.user.photo
		this.time = c.sendtime
		if(c.parent){
			this.parentAutor = c.parent?.user.fullname
		}
	}
	
	Long cid
	/** 发布者 **/
	String autor
	/** 发布者头像 **/
	String autorPhoto
	/** 发布时间 **/
	String time
	/** 回复某人 **/
	String parentAutor
	/*** 回复内容 ***/
	String content
	
}
