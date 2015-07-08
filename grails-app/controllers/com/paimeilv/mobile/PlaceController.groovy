package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

class PlaceController {

	
	def placeService
	
	/** 城市列表 ***/
	def getCityList(){
		//pageNO=1&value=厦门
		String pageno = params.get("pageno")
		String value = params.get("value")
		
		render placeService.getCityList(pageno,value) as JSON
	}
	
	/** 圈子列表 ***/
	def getCircleList(){
		//cityId=1&pageno=1&value=鼓浪屿
		String pageno = params.get("pageno")
		String value = params.get("value")
		String cityId = params.get("cityId")
		
		render placeService.getCircleList(pageno,value,cityId) as JSON
	}
	
	/** 趣处列表 ***/
	def getPlaceList(){
		//cityId=1&pageno=1&value=鼓浪屿
		String pageno = params.get("pageno")
		String value = params.get("value")
		String circleId = params.get("circleId")
		
		render placeService.getPlaceList(pageno,value,circleId) as JSON
	}
	
	/** 趣处详情 ***/
	def getPlace(){
		//pId=1
		String pId = params.get("pId")
		if(!pId||"".equals(pId.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return 
		}
		try {
			render placeService.getPlace(Long.valueOf(pId)) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 分享的趣处图片列表 ***/
	def getPlaceImageList(){
		String pId = params.get("pId")
		String userId = params.get("userId")
		String pageno = params.get("pageno")
		
		String orderby=params.get("orderby")
		
		if((!pId||"".equals(pId.trim()))&&(!userId||"".equals(userId.trim()))){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		try {
			Long pid 
			if(pId) pid =Long.valueOf(pId)
			
			Long uid
			if(userId) uid =Long.valueOf(userId)
			render placeService.getPlaceImageList(pid,uid,pageno,orderby) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 趣处的评论列表 ***/
	def getCardList(){
		String pId = params.get("pId")
		String pageno = params.get("pageno")
		
		String accesstoken = params.get("accesstoken")
		
		if(!pId||"".equals(pId.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		
		try {
			Long pid
			if(pId) pid =Long.valueOf(pId)
			
			render placeService.getCardList(pid,pageno,accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 用户对趣处评分 详情***/
	def getCard(){
		String cId = params.get("cId")
		if(!cId||"".equals(cId.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		try {
			render placeService.getCard(Long.valueOf(cId)) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 图片详情 ***/
	def getImage(){
		//imgId=1&accesstoken=298yd……
		String imgId = params.get("imgId")
		String accesstoken = params.get("accesstoken")
		if(!imgId||"".equals(imgId.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		try {
			render placeService.getImage(Long.valueOf(imgId),accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 获取评论列表 ****/
	def getCommentList(){
		String imgId = params.get("imgId")
		String type = params.get("type")
		String articleId = params.get("articleId")
		String pageno = params.get("pageno")
		if(!type||"".equals(type.trim())){
			Request req=new Request(false,"查询类型不能为空","error",null)
			render req as JSON
			return
		}
		if((!imgId||"".equals(imgId.trim()))&&(!articleId||"".equals(articleId.trim()))){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		try {
			Long imid 
			if(imgId) imid =Long.valueOf(imgId)
			
			Long aid
			if(articleId) aid =Long.valueOf(articleId)
			render placeService.getCommentList(type,imid,aid,pageno) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
		
	}
}
