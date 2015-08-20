package com.paimeilv.mobile

import grails.converters.JSON

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.paimeilv.CheckUtil
import com.paimeilv.basic.User
import com.paimeilv.basic.UserProfile;
import com.paimeilv.bean.Request

class MregisterController {
	
	def registerService

    def index() { 
		if("POST".equals(request.getMethod())){
			Map map = registerService.register(params)
			if(map.get("user")){
				render(new Request(true,null,"注册成功",map.get("user")) as JSON)
			}else{
			 	render(new Request(false,"注册失败",null,map) as JSON)
			}
		} else{
				render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
	
	/**
	 * 获取验证码
	 * ****/
	def getCaptcha(){
		
		String username = params.get("username")//用户名
		String method  = params.get("method") //方法名
		
		if(!username||"".equals(username.trim())||!method||"".equals(method.trim())||(!CheckUtil.checkTel(username)&&!CheckUtil.checkEmail(username))){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return 
		}
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"))
		
		String templateId 
		if("register".equals(method)){
			templateId = properties.getProperty("sms_reg_templateId")
		}else{
			templateId = properties.getProperty("sms_find_templateId")
		}
		def config = grailsApplication.config
		Map m = registerService.getCaptcha(username,templateId,request,config,method)
		
		Request req=new Request(true,null,"success",m)
		if(!m.get("result")){
			req=new Request(false,"获取验证码失败","error",m)
		}else if(!"000000".equals(m.get("result"))){
			req=new Request(false,"接口错误","error",m)
		}
		render req as JSON
	}
	
	/** 重置密码 */
	def resertPsw(){
		String resType = params.get("resType")
		String tel = params.get("tel")
		String email = params.get("email")
		String newpsw = params.get("newpsw")
		String captcha = params.get("captcha")
		
		/// 验证参数 ////
		if(resType&&!"".equals(resType.trim())&&newpsw&&!"".equals(newpsw.trim())&&captcha&&!"".equals(captcha.trim())){
			if("tel".equals(resType)&&!CheckUtil.checkTel(tel)){
				Request req=new Request(false,"手机号格式不正确","error",null)
				render req as JSON
				return
			}else if ("email".equals(resType)&&!CheckUtil.checkEmail(email)){
				Request req=new Request(false,"邮箱格式不正确","error",null)
				render req as JSON
				return
			}
		}else{
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		
		Map map = new HashMap()
		if("tel".equals(resType)) map.put("username", tel)
		if("email".equals(resType)) map.put("username", email)
		
		map.put("newpsw", newpsw)
		map.put("captcha", captcha)
		Map rm = registerService.modifyPassword(map)
		
		Request req
		if(rm.get("error")){
			req=new Request(false,"重置失败","error",rm)
		}else{
			req=new Request(true,"重置成功","success",rm)
		}
		render req as JSON
	}
	
	/** 验证唯一性 ***/
	def isUnique(){
		String type = params.get("type")
		String username = params.get("username")
		if(!username||"".equals(username.trim())||!type||"".equals(type.trim())){
			Request req=new Request(false,"参数错误","error",null)
			render req as JSON
			return
		}
		
		if("username".equals(type)){
			User user = User.findByUsernameOrTelphoneOrEmail(username,username,username)
			if(user) render new Request(false,"登录名已存在","error",null) as JSON
			else render new Request(true,"","登录名可用",null) as JSON
		}else if("fullname".equals(type)){ 
			UserProfile up = UserProfile.findByFullName(username)
			if(up) render new Request(false,"昵称已存在","error",null) as JSON
			else render new Request(true,"","昵称可用",null) as JSON
		}else{
			render new Request(false,"请输入正确的验证唯一性类型参数（username or fullname）","error",null) as JSON
		}
	}
	
	/** 修改用户信息 ***/
	def updateUser() { 
		if("POST".equals(request.getMethod())){
			def user = params.get("user")
			String accesstoken = params.get("accesstoken")
			render registerService.updateUser(user,accesstoken) as JSON
		} else{
			render(new Request(false,"plase request POST Method ",null,null) as JSON)
		}
	}
}
