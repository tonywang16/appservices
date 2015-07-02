package com.paimeilv

import grails.converters.JSON

import java.text.SimpleDateFormat

import com.paimeilv.basic.User

class GrantController {

	def oauthService
	
	def redisService
//	def springSecurityService
	
	def beforeInterceptor = [action:this.&auth,except: ['getJsConfig','checkBind','setsession']] //拦截器定义
	
	/*** 微信登录拦截 ***/
	def auth() {
		
		//从请求头获取到token
		String token =request.getHeader("longin_token")
		if(!token||"".equals(token)){
			
			render -1 as JSON
			return false
		}
		
		redisService.iterator()
		assert "bar" == redisService.foo
		
		
		Long userId = session["SESSION_USER_ID"]
		User user = User.get(userId)
		String uid = params["uid"]
		if(!user){
	   		String weixinurl = oauthService.getweiopenurl(uid)
		   redirect(url:weixinurl)
		   return false
	   }
   }
	
	def setsession ={
		String url = params.get("furl")
		session["redparams"] = url
		render 0
	}

	def getJsConfig(){
		String url = params.get("furl")
		
		Map map = oauthService.getJsConfig(url);
		
		render map as JSON
	}
}
