package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

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
		//vaule=建议内容&accesstoken=298yd12dsa2gvv3
		String value = params.get("value")
		String accesstoken = params.get("accesstoken")
		if("POST".equals(request.getMethod())){
			render snsService.feedback(value,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 广告位 ***/
	def getAdvert(){
		String cid = params.get("cid")
		
		Long fid = null
		if(cid&&!"".equals(cid)) fid = Long.valueOf(cid)
		
		render snsService.getAdvert(fid) as JSON
	}
}
