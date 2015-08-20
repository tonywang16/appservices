package com.paimeilv.mobile

import grails.converters.JSON

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.paimeilv.basic.User
import com.paimeilv.bean.AutorBean
import com.paimeilv.bean.Request



class OauthController {
	def oauthService
	
	def springSecurityService
	
	/** 微信验证 **/
	def checkWeixin={
		String code = params.get("code")
		String equip = params.get("equip")
		String type = params.get("type")
		String accesstoken = params.get("accesstoken")
		AutorBean bean = oauthService.getOauth2ByWeixin(request,code)
		if(bean.state==-1){
			render(new Request(false,"","微信授权失败",null) as JSON)
		}else{
			if("login".equals(type)){
				User user = oauthService.getOpenUser(bean)
				com.paimeilv.json.bean.User u = new com.paimeilv.json.bean.User(user,equip)
				render(new Request(true,"login success",null,u) as JSON)
			}else if("bind".equals(type)){
				Map m = oauthService.bindUser(bean, accesstoken)
				render(new Request(m.get("result"),m.get("msg"),null,m) as JSON)
			}
		}
	}
	
	/** 微博验证 **/
	def checkSina={
		String code = params.get("code")
		String equip = params.get("equip")
		String type = params.get("type")
		String accesstoken = params.get("accesstoken")
		AutorBean bean = oauthService.getOauth2ByWeibo(request,code)
		if(bean.state==-1){
			render(new Request(false,"","微博授权失败",null) as JSON)
		}else{
			if("login".equals(type)){
				User user = oauthService.getOpenUser(bean)
				com.paimeilv.json.bean.User u = new com.paimeilv.json.bean.User(user,equip)
				render(new Request(true,"login success",null,u) as JSON)
			}else if("bind".equals(type)){
				Map m = oauthService.bindUser(bean, accesstoken)
				render(new Request(m.get("result"),m.get("msg"),null,m) as JSON)
			}
		}
	}
	
	def getSecret={
		Map weixinmap = new HashMap()
		def weixinProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String weixinAppid = weixinProperties.getProperty("appid") //微信appid
		String weixinSecret = weixinProperties.getProperty("secret")//微信secret
		weixinmap.put("appid", weixinAppid)
		weixinmap.put("secret", weixinSecret)
		
		def weiboProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"))
		String weiboAppid = weiboProperties.getProperty("client_ID") //微博appid
		String weiboSecret = weiboProperties.getProperty("client_SERCRET")//微博secret
		Map weibomap = new HashMap()
		weibomap.put("appid", weiboAppid)
		weibomap.put("secret", weiboSecret)
		
		Map map = new HashMap()
		map.put("weixin", weixinmap)
		map.put("weibo", weibomap)
		
		render(new Request(true,"success",null,map) as JSON)
	}
}