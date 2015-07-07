package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

class OperateController {

	def operateService
	/** 发现趣处 ***/
    def proposalPlace() {
		def place = params.get("place")
		String accesstoken = params.get("accesstoken")
		if("POST".equals(request.getMethod())){
			render operateService.proposalPlace(place,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 趣处评分的添加与修改 **/
	def saveOrUpdateCard(){
		
	}
	/** 评论图片 ***/
	def commImage(){
		
	}
	
	/** 评论文章 ***/
	def commArticle(){
		
	}
}
