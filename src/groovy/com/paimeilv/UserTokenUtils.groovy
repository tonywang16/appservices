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
	
	public static Map checkUserToken(String token){
		
		UserToken ut = UserToken.findByAccessToken(token)
		Map map = new HashMap()
		
		if(!ut) {
			map.put("result",false)
			map.put("msg","令牌错误")
			return map
		}
		
		Date now = new Date()
		Map dmap = DateUtils.comparison(now, ut.time)
		Long dd = dmap.get("d"); //天数
		Long hh = dmap.get("h");//小时
		Long mm = dmap.get("m"); //分钟
		Long ss = dmap.get("s"); //秒
		if(dd<30){
			map.put("result",true)
			map.put("userToken", ut)
		}else{
			map.put("result",false)
			map.put("msg","令牌失效")
		}
		return map
	}
}
