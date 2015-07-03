package com.paimeilv.mobile

import grails.converters.JSON

class SnsController {

	def snsService
	
	/** 收藏 ***/
	def favorite(){
		String type = params.get("type")
		String formId = params.get("formId")
		String accesstoken = params.get("accesstoken")
		Long fid = null
		if(formId&&!"".equals(formId)) fid = Long.valueOf(formId)
		
		render snsService.favorite(type,fid,accesstoken) as JSON
	}
	
	/** 取消收藏 ***/
	def delfavorite(){
		String type = params.get("type")
		String formId = params.get("formId")
		String accesstoken = params.get("accesstoken")
		Long fid = null
		if(formId&&!"".equals(formId)) fid = Long.valueOf(formId)
		
		render snsService.delfavorite(type,fid,accesstoken) as JSON
	}
	
	/** 点赞 ***/
	def praise(){
		String type = params.get("type")
		String formId = params.get("formId")
		String accesstoken = params.get("accesstoken")
		Long fid = null
		if(formId&&!"".equals(formId)) {fid = Long.valueOf(formId)}
		
		render snsService.praise(type,fid,accesstoken) as JSON
	}
	 
	/** 反馈 ***/
	def feedback(){
		
	}
	
	/** 广告位 ***/
	def getAdvert(){
		
	}
}
