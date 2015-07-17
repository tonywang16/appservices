package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

class PersonalController {

	def personalService
	/** 订单列表 ***/
    def getOrderList() {
		
	}
	
	/** 趣处评分列表 ***/
	def getCardList() {
		String pageno = params.get("pageno")
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getCardList(pageno,accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 所建议的趣处列表 ***/
	def getProposalPlaceList() {
		String pageno = params.get("pageno")
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getProposalPlaceList(pageno,accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 所建议的趣处详情 ***/
	def getProposalPlace() {
		String pId = params.get("pId")
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getProposalPlace(pId,accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	/** 收藏列表 ***/
	def getFavoriteList() {
		String pageno = params.get("pageno")
		String accesstoken = params.get("accesstoken")
		String type = params.get("type")
		try {
			render personalService.getFavoriteList(type,accesstoken,pageno) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
		
	}
	
	/** 用户信息 ***/
	def getUser() {
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getUser(accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	
	/** 修改用户信息 ***/
	def updateUser() { 
		if("POST".equals(request.getMethod())){
			def user = params.get("user")
			String accesstoken = params.get("accesstoken")
			render personalService.updateUser(user,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 用户所拥有的收藏数量、订单数量、趣处评分及发现的趣处的数量 ***/
	def getUserAmount() {
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getUserAmount(accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
	
	
	/** 消息中心 ***/
	def getMessageList() {
		String pageno = params.get("pageno")
		String accesstoken = params.get("accesstoken")
		try {
			render personalService.getMessageList(pageno,accesstoken) as JSON
		} catch (Exception e) {
			Request req=new Request(false,e.getMessage(),"error",null)
			render req as JSON
		}
	}
}
