package com.paimeilv.sns

import grails.transaction.Transactional

import com.paimeilv.UserTokenUtils
import com.paimeilv.basic.Article
import com.paimeilv.basic.Circle
import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Image
import com.paimeilv.basic.Place
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.Praise
import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Request
import com.paimeilv.config.Advert
import com.paimeilv.config.Feedback
import com.paimeilv.json.bean.AdvertJson

@Transactional
class SnsService {
	
	/** 收藏 **/
	def favorite(String type,Long formId,String accesstoken){
		Request req
		if(!type||"".equals(type.trim())||!formId||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		if("image".equals(type)){
			Image image = Image.get(formId)
			if(image){
				Favorite f = Favorite.findWhere(image:image,user:user) ?: new Favorite(image:image,user:user).save(flush:true) //图片收藏
			}else{
				req=new Request(false,"没找到该图片","error",null)
				return req
			}
		}else if("article".equals(type)){
			Article article = Article.get(formId)
			if(article){
				Favorite f = Favorite.findWhere(article:article,user:user) ?: new Favorite(article:article,user:user).save(flush:true) //资讯收藏
			}else{
				req=new Request(false,"没找到该资讯","error",null)
				return req
			}
		}else if("place".equals(type)){
			Place place = Place.get(formId)
			
			if(place){
				Favorite f = Favorite.findWhere(place:place,user:user) ?: new Favorite(place:place,user:user).save(flush:true) //趣处收藏
			}else{
				req=new Request(false,"没找到该趣处","error",null)
				return req
			}
		}else if("card".equals(type)){
			Postcard card = Postcard.get(formId)
			if(card){
				Favorite f = Favorite.findWhere(card:card,user:user) ?: new Favorite(card:card,user:user).save(flush:true) //评分收藏
			}else{
				req=new Request(false,"没找到该趣处评分","error",null)
				return req
			}
		}else{
			req=new Request(false,"请输入正确的收藏类型参数","error",null)
			return req
		}
		
		req=new Request(true,"","success",null)
		return req
	}
	
	def delfavorite(String type,Long formId,String accesstoken){
		Request req
		if(!type||"".equals(type.trim())||!formId||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		if("image".equals(type)){
			Image image = Image.get(formId)
			if(image){
				Favorite f = Favorite.findWhere(image:image,user:user) //图片收藏
				f?.delete(flush:true)
			}else{
				req=new Request(false,"没找到该图片","error",null)
				return req
			}
		}else if("article".equals(type)){
			Article article = Article.get(formId)
			if(article){
				Favorite f = Favorite.findWhere(article:article,user:user) //资讯收藏
				f?.delete(flush:true)
			}else{
				req=new Request(false,"没找到该资讯","error",null)
				return req
			}
		}else if("place".equals(type)){
			Place place = Place.get(formId)
			
			if(place){
				Favorite f = Favorite.findWhere(place:place,user:user) //趣处收藏
				f?.delete(flush:true)
			}else{
				req=new Request(false,"没找到该趣处","error",null)
				return req
			}
		}else if("card".equals(type)){
			Postcard card = Postcard.get(formId)
			if(card){
				Favorite f = Favorite.findWhere(card:card,user:user) //评分收藏
				f?.delete(flush:true)
			}else{
				req=new Request(false,"没找到该趣处评分","error",null)
				return req
			}
		}else{
			req=new Request(false,"请输入正确的收藏类型参数","error",null)
			return req
		}
		
		req=new Request(true,"","success",null)
		return req
	}
	
	def praise(String type,Long formId,String accesstoken){
		Request req
		if(!type||"".equals(type.trim())||!formId){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		if("image".equals(type)){
			Image image = Image.get(formId)
			if(image){
				if(user) Praise p = Praise.findWhere(image:image,user:user)?:new Favorite(image:image,user:user).save(flush:true)  //图片
				else new Favorite(image:image).save(flush:true)  
			}else{
				req=new Request(false,"该图片已被删除","error",null)
				return req
			}
		}else if("card".equals(type)){
			Postcard card = Postcard.get(formId)
			if(card){
				if(user)  Praise p = Praise.findWhere(card:card,user:user)?: new Favorite(card:card,user:user).save(flush:true) //评分
				else new Favorite(card:card).save(flush:true)
			}else{
				req=new Request(false,"该评分已被删除","error",null)
				return req
			}
		}else{
			req=new Request(false,"请输入正确的点赞类型参数","error",null)
			return req
		}
	}
	
	def feedback(String value,String accesstoken){
		Request req
		if(!value||"".equals(value.trim())||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		if(value.length()>250){
			req=new Request(false,"意见字符不能超过250字","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		Feedback f = new Feedback()
		f.value = value
		f.user = user
		f.save(flush:true)
		
		if(f?.id&&f?.id>0){
			req=new Request(true,"","success",null)
			return req
		}else{
			req=new Request(false,"反馈失败(请联系管理员)","error",null)
			return req
		}
	}
	
	def getAdvert(Long cid){
		Request req
		if(!cid){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		Circle c = Circle.get(cid)
		List<Advert> alist = Advert.findAllWhere(area:c,[max:5, offset:0])
		List<AdvertJson> ajlist = new ArrayList<AdvertJson>()
		if(alist&&alist.size()>0){
			for (a in alist) {
				AdvertJson aj = new AdvertJson(a)
				ajlist.add(aj)
			}
			req=new Request(true,"","success",ajlist)
			return req
		}else{
			req=new Request(false,"","没有数据",ajlist)
			return req
		}
	}

}
