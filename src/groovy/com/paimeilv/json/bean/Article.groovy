package com.paimeilv.json.bean

import com.paimeilv.basic.Image

/** 文章 ***/
class Article {
	
	public Article(com.paimeilv.basic.Article a){
		if(!a) return
		this.aid = a.id
		this.title = a.title
		this.autor = a.user.fullname
		if(a.image){
			Iterator i=a.image.iterator();
			while(i.hasNext()){
				Image img=i.next();
				aImg+=img.pathstr+"|"
			}
			if(aImg&&!"".equals(aImg)) aImg = aImg.substring(0, aImg.length()-1)
		}
		
		this.time = a.sendtime
		this.favoNum=a.favorite?.size()?:0
		this.comNum=a.comment?.size()?:0
	}

	/** 文章ID ***/
	Long aid
	
	/** 标题 **/
	String title
	
	/** 发布者 **/
	String autor
	
	/** 发布时间 **/
	String time
	
	/***图片地址（多个可用”|”分割）**/
	String aImg
	
	/** 收藏数 ***/
	Long favoNum
	
	/** 评论数 ***/
	Long comNum
}
