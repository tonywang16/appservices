package com.paimeilv

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken

class UserTokenUtils {
	public static UserToken getUserToken(String equip,User user){
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"))
		String appKey = properties.getProperty("app_key")
		
		if(!user) return null
		
		UserToken ut = UserToken.findWhere(user:user,equipment:equip)
		
		if(!ut){
			/***
			 * 令牌生成规则:userId+key+设备号，然后sha1加密
			 * ***/
			String str = user.id+appKey+equip
			String token = TokenUtils.getSha1(str)
			
			ut =  new UserToken()
			ut.user = user
			ut.accessToken = token
			ut.time = new Date()
			ut.equipment = equip
			
			ut.save(flush:true)
		}
		return ut
	}
}
