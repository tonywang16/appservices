package com.paimeilv

import grails.transaction.Transactional

import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.paimeilv.basic.Role
import com.paimeilv.basic.User
import com.paimeilv.basic.UserOpenID
import com.paimeilv.basic.UserProfile
import com.paimeilv.basic.UserRole
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.AutorBean
import com.paimeilv.config.WeixinAccessToken

@Transactional
class OauthService {
	
	/** 获取Oauth2协议的OpenID 与 AccessToken by微信 **/
	public AutorBean  getOauth2ByWeixin(HttpServletRequest  request,String pcode){
		
		AutorBean bean = new AutorBean()
		String code = pcode
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String accessTokenUrl = properties.getProperty("accessToken")
		String appid = properties.getProperty("appid")
		String secret = properties.getProperty("secret")
		String userinfo = properties.getProperty("userinfo")
		
		String url = accessTokenUrl +"?appid="+ appid +"&secret="+ secret +"&code=" + code+"&grant_type=authorization_code"
		JSONObject json = ConnectUtils.doGetStr(url)
		Map map = (Map)json
		String accessToken= map.get("access_token");
		String openId = map.get("openid");
		int returnNum =0
		if(accessToken&&openId){
			
			url = userinfo +"?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN"
			json = ConnectUtils.doGetStr(url)
			map = (Map)json
			
			//获取用户信息
			if("0".equals(map.get("sex"))){
				map.put("sex","f");
			}
			
			bean.openId=openId
			bean.openType="weixin"
			bean.username=map.get("nickname")
			bean.photo = map.get("headimgurl")
			bean.gender = map.get("sex")
		}else{
			returnNum =-1
		}
		bean.state = returnNum
		return bean
	}
	
	/** 获取Oauth2协议的OpenID 与 AccessToken by微博 **/
	public AutorBean  getOauth2ByWeibo(HttpServletRequest  request,String pcode){
		String username
		String userPhotoUrl
		String gender
		User user =null
		String code =pcode
		AutorBean bean = new AutorBean()
		int returnNum =0
		if(!code||"".equals(code)) {
			returnNum=-1
		}else{
			weibo4j.Oauth oauth = new weibo4j.Oauth();
			String tokenStr = oauth.getAccessTokenByCode(code).toString();
			String[] str = tokenStr.split(","); //截取字符串，获得sccessToken和uid
			String accessToken= null;
			String uid = null;
			long tokenExpireIn = 0L;
			
			weibo4j.Users um = new weibo4j.Users();
			if (accessToken.equals("")) {
				returnNum=-1
			}else{
			   // AccessToken token = oauth.getAccessTokenByCode(code);
				accessToken =  str[0].split("=")[1];
				String[] str1 = str[3].split("]");
				uid = str1[0].split("=")[1];
				//tokenExpireIn = oauth.getExpireIn();
				um.client.setToken(accessToken);
				weibo4j.model.User weiboInfo = um.showUserById(uid);
				username = weiboInfo.getScreenName();
				userPhotoUrl = weiboInfo.getProfileImageUrl();
				gender = weiboInfo.getGender()
				
				bean.accessToken=accessToken
				bean.openId=uid
				bean.openType="weibo"
				bean.username=username
	//			bean.email = email
				bean.photo = userPhotoUrl
				bean.gender = gender
			}
		}
		
		bean.state = returnNum
		//bean.openType=type
		
		UserOpenID up=UserOpenID.findWhere(openId:bean.openId,openType:bean.openType)
		if(up) bean.isexist = 1  //如果已注册则为1
		return bean
	}
	
	
	
	def getOpenUser(AutorBean bean){
		
		UserOpenID uopen  = UserOpenID.findWhere(openId:bean.openId,openType:bean.openType)
		User user =uopen?.user
		if(user){
			return user
		}
		
		user =new User()
		String u_name = findByUserName(bean.username)
		user.setUsername(u_name)
		user.setPassword(bean.openId)
		user.setEnabled(true)
		user.save(flush:true)
		
		uopen = new UserOpenID()
		uopen.accessToken = bean.accessToken
		uopen.openId = bean.openId
		uopen.openType = bean.openType
		uopen.openName = bean.username
		uopen.openUrl = bean.photo

		uopen.user = user
		uopen.save(flush:true)
		
		UserProfile userp =user?.userProfile
		if(!userp){
			userp = new UserProfile()
			userp.fullName=u_name
			if("f".equals(bean.gender)) userp.gender = "W"
			//userp.user = user
			userp.userPhotoUrl = bean.photo
			userp.save(flush:true)
		}
		user.userProfile =userp
		user.save(flush:true)
		
		def guestRole = Role.findByAuthority("ROLE_USER")  //普通注册用户权限
		UserRole.create(user, guestRole)
		
		return user
	}
	
	/** 社交账号绑定 ***/
	def bindUser(AutorBean bean,String accesstoken){
		Map rm = new HashMap()
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			return cm
		}
		UserOpenID uopen  = UserOpenID.findWhere(openId:bean.openId,openType:bean.openType)
		
		if(uopen){
			rm.put("result", false)
			rm.put("msg", "已被绑定")
			return rm
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		uopen = new UserOpenID()
		uopen.accessToken = bean.accessToken
		uopen.openId = bean.openId
		uopen.openType = bean.openType
		uopen.openName = bean.username
		uopen.openUrl = bean.photo
		uopen.user = user
		uopen.save(flush:true)
		
		rm.put("result", true)
		rm.put("msg", "绑定成功")
		return rm
	}
	
	
	
	/** 获取accessToken **/
	public String getAccessToken(){
		
		WeixinAccessToken accessToken = WeixinAccessToken.first()
		if(accessToken){
			Date now = new Date()
			Date old = accessToken.lastUpdated
			Long ms = now.getTime() - old.getTime()
			Long mm = ms/1000//获得两时间的秒差
			if(mm<(accessToken.expiresIn-100)){ //相差大于100秒则不重新获取AccessToken
				return accessToken.token
			}
		}
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String accessTokenUrl = properties.getProperty("mp_accessToken")
		String appid = properties.getProperty("mp_appid")
		String secret = properties.getProperty("mp_secret")
		//获取accessToken
		Map<String,String> map = new HashMap<String,String>();
		
		
		String url = accessTokenUrl +"?grant_type=client_credential&appid="+appid+"&secret="+secret
		JSONObject json = ConnectUtils.doGetStr(url)
		map = (Map)json
		String token = map.get("access_token")
		String expiresIn = map.get("expires_in")
		
		if(!accessToken) accessToken = new WeixinAccessToken()
		accessToken.token = token
		accessToken.expiresIn = Long.valueOf(expiresIn)
		accessToken.save(flush:true)
		
		return token
	}
	
	
	
	public AutorBean getOauth2ByWeixin(String openID){
		AutorBean bean = new AutorBean()
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String accessTokenUrl = properties.getProperty("mp_accessToken")
		String appid = properties.getProperty("mp_appid")
		String secret = properties.getProperty("mp_secret")
		String userinfo = properties.getProperty("mp_userinfo")
		
		Map<String,String> map = new HashMap<String,String>();
		
		String token = getAccessToken()
		if(token){
			
			String url = userinfo +"?access_token="+token+"&openid="+openID+"&lang=zh_CN"
			JSONObject json = ConnectUtils.doGetStr(url)
			map = (Map)json
			
			if("0".equals(map.get("sex"))){
				map.put("sex","f")
			}
			
			bean.accessToken=token
			bean.openId=openID
			bean.openType="weixin"
			bean.username=map.get("nickname")
			bean.photo = map.get("headimgurl")
			bean.gender = map.get("sex")
		}
		return bean
	}
	
	/** 获取微信授权地址**/
	public String getweiopenurl(String uid){
		
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String redirect_uri = properties.getProperty("redirect_uri")
		String url = java.net.URLEncoder.encode(redirect_uri, "utf-8")
		String authorizeURL = properties.getProperty("authorizeURL")
//		String authorizeURL = properties.getProperty("pc_authorizeURL")
		String appid = properties.getProperty("appid")
		String weixinurl = authorizeURL +"?appid="+appid+"&redirect_uri="+url+"&response_type=code&scope=snsapi_login&state=weixin"
		if(uid&&!"".equals(uid)) weixinurl+="&uid"+uid
		weixinurl+="#wechat_redirect"
		
		return weixinurl
	}
	
	
	public String findByUserName(String username){
		String name = toUserName()
		
		if(!username||"".equals(username)) return "拍美旅用户"+name
		String uname = username.replaceAll("[^a-zA-Z_\u4e00-\u9fa5]", "")
		if(User.findWhere(username:username)==null){
			uname = username
		}else{
			
			uname = username+name
		}
		return uname
	}
	
	public String toUserName(){
		long useraccount = (System.currentTimeMillis()%1000000);
		String account = String.valueOf(useraccount)
		return account;
	}
}
