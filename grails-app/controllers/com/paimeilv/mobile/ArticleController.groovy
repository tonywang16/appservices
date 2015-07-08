package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

class ArticleController {

	def articleService
	/** 资讯列表 ****/
    def getArticleList() { 
		String pageno = params.get("pageno")
		String type = params.get("type")
		
		render articleService.getArticleList(pageno,type) as JSON
	}
	
	/** 资讯详情 ****/
	def getArticle(){
		String aid = params.get("aId")
		String accesstoken = params.get("accesstoken")
		if(!aid||"".equals(aid.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		
		render articleService.getArticle(Long.valueOf(aid),accesstoken) as JSON
	}
}
