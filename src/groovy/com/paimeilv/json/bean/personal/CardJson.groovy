package com.paimeilv.json.bean.personal

import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.Praise

class CardJson {
	public CardJson(Postcard c){
		if(!c) return
		this.cardId=c.id
		this.autor = c.user.fullname
		this.autorPhoto =c.user.photo
		this.score =c.score
		this.comment =c.comment
		this.time = c.sendtime
		this.favoNum = c.favorite?.size()?:0
		this.praiseNum=c.praise?.size()?:0
		Favorite f = Favorite.findWhere(card:c,user:c.user)
		if(f) isf =  true
		Praise p = Praise.findWhere(card:c,user:c.user)
		if(p) isp=  true
		
		this.placeId = c.place.id
		this.plcaeName = c.place.name
		
		this.plcaeAddress = c.place.circle.city.value+" "+c.place.circle.name
		
		this.plcaeCover = c.place.cover
	}
	
	Long cardId
	/** 趣处ID ***/
	Long placeId
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
	/** 收藏数 ***/
	Long favoNum
	/** 点赞数 ***/
	Long praiseNum
	
	/** 趣处名称 **/
	String plcaeName
	
	/** 趣处地址 **/
	String plcaeAddress
	
	/** 趣处封面 **/
	String plcaeCover
	
}
