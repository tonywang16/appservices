package com.paimeilv

import com.paimeilv.basic.User
import com.paimeilv.bean.AutorBean



class OauthController {
	def oauthService
	
	def springSecurityService
	
	def checkWeixin={
		String code = params.get("code")
		String uid =request.getSession().getAttribute("UID")  //请求方式（注册，登录，绑定）
		AutorBean bean = oauthService.getOauth2ByWeixin(request,code)
		if(bean.state==-1){
			render "微信授权失败"
		}else{
			User user = User.findWhere(openId:bean.openId)
			if(!user) {
				user = new User()
				user.name = bean.username
				user.openId = bean.openId
				user.photo = bean.photo
				user.save(flush:true)
			}
			session["SESSION_USER_ID"]=user?.id
			redirect(controller:"vote",action:"share",params:params)
		}
	}
}