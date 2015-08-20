package com.paimeilv.mobile

import grails.converters.JSON

import com.paimeilv.bean.Request

class OperateController {

	def operateService
	
	def qiNiuService
	/** 发现趣处 ***/
    def proposalPlace() {

		if("POST".equals(request.getMethod())){
			def place = params.get("place")
			String accesstoken = params.get("accesstoken")
			render operateService.proposalPlace(place,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 趣处评分的添加与修改 **/
	def saveOrUpdateCard(){
		
		if("POST".equals(request.getMethod())){
			def card = params.get("card")
			String accesstoken = params.get("accesstoken")
			render operateService.saveOrUpdateCard(card,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}

	}
	/** 评论图片 ***/
	def commImage(){
		if("POST".equals(request.getMethod())){
			String imgId = params.get("imgId")
			String content = params.get("content")
			String accesstoken = params.get("accesstoken")
			String  commId = params.get("commId")
			
			Integer imgid 
			if(imgId&&!"".equals(imgId.trim())){
				imgid = Integer.valueOf(imgId)
			}
			
			Integer cid
			if(commId&&!"".equals(commId.trim())){
				cid = Integer.valueOf(commId)
			}
			render operateService.commImage(imgid,content,cid,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 评论文章 ***/
	def commArticle(){
		if("POST".equals(request.getMethod())){
			String aId = params.get("aId")
			String content = params.get("content")
			String accesstoken = params.get("accesstoken")
			String  commId = params.get("commId")
			
			Integer aid
			if(aId&&!"".equals(aId.trim())){
				aid = Integer.valueOf(aId)
			}
			
			Integer cid
			if(commId&&!"".equals(commId.trim())){
				cid = Integer.valueOf(commId)
			}
			render operateService.commArticle(aid,content,cid,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/** 获取上传令牌 ***/
	def getQiniuToenk={
		String token = qiNiuService.uptoken(qiNiuService.TEMP_BUCKET)//上传令牌获取
		Map map = new HashMap();
		map.put("token", token)
		map.put("tempBucket", qiNiuService.TEMP_BUCKET)
		render(new Request(true,"","success",map) as JSON)
	}
}
