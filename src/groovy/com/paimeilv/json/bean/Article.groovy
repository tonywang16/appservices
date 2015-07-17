package com.paimeilv.json.bean

import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Image
import com.paimeilv.basic.Praise

/** 文章 ***/
class Article {
	
	public Article(com.paimeilv.basic.Article a,com.paimeilv.basic.User u){
		if(!a) return
		this.aid = a.id
		this.title = a.title
		this.autor = a.user.fullname
		
		
		if(a.image){
			aImg = new ArrayList<String>()
			Iterator i=a.image.iterator();
			while(i.hasNext()){
				Image img=i.next();
				aImg.add(img.pathstr)
			}
		}
		
		this.time = a.sendtime
		this.favoNum=a.favorite?.size()?:0
		this.comNum=a.comment?.size()?:0
		
		if(u){
			Favorite f = Favorite.findWhere(article:a,user:u)
			if(f) isf =  true
			Praise p = Praise.findWhere(article:a,user:u)
			if(p) isp=  true
		}
	}

	/** 文章ID ***/
	Long aid
	/** 标题 **/
	String title
	/** 发布者 **/
	String autor
	/** 发布时间 **/
	String time
	/*** 图片地址 **/
	List<String> aImg 
	/** 收藏数 ***/
	Long favoNum
	/** 评论数 ***/
	Long comNum
	/** 是否收藏 ***/
	Boolean isf=false
	/** 是否点赞 ***/
	Boolean isp=false
}
