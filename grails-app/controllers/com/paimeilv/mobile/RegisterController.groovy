package com.paimeilv.mobile

import grails.converters.JSON

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.paimeilv.basic.User
import com.paimeilv.bean.Request

class RegisterController {
	
	def registerService

    def index() { 
		if("POST".equals(request.getMethod())){
			Map map = registerService.register(params)
			if(map.get("user")){
				com.paimeilv.json.bean.User u = new com.paimeilv.json.bean.User(map.get("user"),null)
				render(new Request(true,null,"注册成功",u) as JSON)
			}else{
			 	render(new Request(true,"注册失败",null,map) as JSON)
			}
		} else{
				render(new Request(true,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/**
	 * 获取验证码
	 * ****/
	def getcode(){
		
		String telphone = params.get("telphone")//手机号
		String method  = params.get("method") //方法名
		
		if(!telphone||"".equals(telphone.trim())||!method||"".equals(method.trim())){
			Request req=new Request(true,"参数错误","error",null)
			
			render req as JSON
			return 
		}
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"))
		
		String templateId 
		if("register".equals(telphone)){
			templateId = properties.getProperty("sms_reg_templateId")
		}else{
			templateId = properties.getProperty("sms_find_templateId")
		}
		
		Map m = registerService.getCaptcha( telphone, templateId, request, method)
		
		Request req=new Request(true,null,"success",m)
		if(!"000000".equals(m.get("result"))){
			req=new Request(true,"短信接口错误","error",m)
		}
		render req as JSON
	}
}
