package com.paimeilv

import grails.transaction.Transactional

import com.paimeilv.basic.Composite
import com.paimeilv.basic.Image
import com.paimeilv.basic.PlaceTemp
import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Request
import com.paimeilv.config.RootFolder

@Transactional
class OperateService {
	
	def qiNiuService

	def proposalPlace(Map place,String accesstoken){
		Request req
		if(!place||!place?.pname||"".equals(!place?.pname?.trim())||!place?.paddress||"".equals(!place?.paddress?.trim())||!accesstoken||"".equals(!accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		
		String pname = place?.pname //趣处名称
		String paddress = place?.paddress //趣处地址
		String profile = place?.profile //趣处简介
		String pimage = place?.pimage //趣处图片 多图以 "|"分割
		
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		PlaceTemp pt = new PlaceTemp()
		pt.name = pname
		pt.address = paddress
		pt.instruction = profile
		pt.user = user
		pt.save(flush:true)
		String[] imgarray = pimage.split("\\|")
		for (path in imgarray) {
			String  a1 = path.split("//")[1]
			String bucket = a1.substring(0,a1.indexOf("."))
			String key = a1.substring(a1.indexOf("/")+1,a1.length())
			String newkey = String.valueOf(System.currentTimeMillis())
			if(qiNiuService.replace(bucket, QiNiuService.UPLOAD_BUCKET, key, newkey)){
				RootFolder rootfolder = RootFolder.findByBucket(QiNiuService.UPLOAD_BUCKET)
				Image img = new Image()
				img.rootfolder = rootfolder
				img.path = newkey
				img.save(flush:true)
				Composite c =new Composite()
				c.image = img 
				c.pointTemp = pt
				c.save(flush:true)
				img.composite = c
				img.save(flush:true)
			}
		}
		req=new Request(true,"","success",null)
		return req
	}
}
