package com.paimeilv.json.bean

import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.Praise

class CardJson {
	
	public CardJson(Postcard c,User u){
		if(!c) return 
		this.cardId=c.id
		this.autor = c.user.fullname
		this.autorPhoto =c.user.photo
		this.score =c.score
		this.comment =c.comment
		this.time = c.sendtime
		if(u){
			Favorite f = Favorite.findWhere(card:c,user:u)
			if(f) isf =  true
			Praise p = Praise.findWhere(card:c,user:u)
			if(p) isp=  true
		}
	}
	
	Long cardId
	/** 创建人 **/
	String autor
	/*** 创建人头像 ***/
	String autorPhoto
	/** 评分 */
	Double  score
	/** 评论 */
	String comment
	/** 最后修改时间 **/
	String time
	/** 是否收藏 ***/
	Boolean isf=false
	/** 是否点赞 ***/
	Boolean isp=false
}
