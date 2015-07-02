package com.paimeilv

import grails.transaction.Transactional

import javax.servlet.http.HttpServletRequest

import net.sf.json.JSONObject

import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import weibo4j.http.AccessToken

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
		//String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx098e21c5f5021b99&secret=f8ea18d9c0791eb7f33548c491b9d261&code="+code+"&grant_type=authorization_code"
		//获取accessToken
//		String sTotalString =ConnectUtils.sendGet(accessTokenUrl,"appid="+ appid +"&secret="+ secret +"&code=" + code+"&grant_type=authorization_code")
//		JSONObject json = JSONObject.fromObject(sTotalString);
		String url = accessTokenUrl +"?appid="+ appid +"&secret="+ secret +"&code=" + code+"&grant_type=authorization_code"
		//获取accessToken
//		JSONObject json =getJson(url)
		JSONObject json = ConnectUtils.doGetStr(url)
		Map map = (Map)json
//		Map map = reflect(json)
		String accessToken= map.get("access_token");
		String openId = map.get("openid");
		int returnNum =0
		if(accessToken&&openId){
			
//			String sTotalString1 =ConnectUtils.sendGet(userinfo,"access_token="+accessToken+"&openid="+openId+"&lang=zh_CN")
//			json = JSONObject.fromObject(sTotalString1);
			url = userinfo +"?access_token="+accessToken+"&openid="+openId+"&lang=zh_CN"
			json = ConnectUtils.doGetStr(url)
			map = (Map)json
//			map = reflect(json)
			
			//获取用户信息
			if("0".equals(map.get("sex"))){
				map.put("sex","f");
			}
			
			bean.openId=openId
			bean.openType="weixin"
			bean.username=map.get("nickname")
//			bean.email = email
			bean.photo = map.get("headimgurl")
			bean.gender = map.get("sex")
		}else{
			returnNum =-1
		}
		bean.state = returnNum
		return bean
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
//		String sTotalString =ConnectUtils.sendGet(accessTokenUrl,"grant_type=client_credential&appid="+appid+"&secret="+secret)
//		JSONObject json = JSONObject.fromObject(sTotalString);
		Map<String,String> map = new HashMap<String,String>();
		
		
		String url = accessTokenUrl +"?grant_type=client_credential&appid="+appid+"&secret="+secret
		JSONObject json = ConnectUtils.doGetStr(url)
		map = (Map)json
		String token = map.get("access_token")
		String expiresIn = map.get("expires_in")
		
		if(!accessToken) accessToken = new AccessToken()
		accessToken.token = token
		accessToken.expiresIn = Long.valueOf(expiresIn)
		accessToken.save(flush:true)
		
		return token
	}
	
	public Map getJsConfig(String url){
		if(url.lastIndexOf("#")!=-1){
			url = url.substring(0,url.indexOf("#")-1)
		}
		Map resl = new HashMap()
		String noncestr = CheckUtils.randomString(16)
		String timestamp =  new Date().getTime()
		String token = getAccessToken()
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String appid = properties.getProperty("mp_appid")
		String ticketUrl = properties.getProperty("mp_ticket_get")
		
//		String resp =ConnectUtils.sendGet(ticketUrl,"access_token="+token+"&type=jsapi");
//		JSONObject json = JSONObject.fromObject(resp);
		
		String ticketUrl_all = ticketUrl +"?access_token="+token+"&type=jsapi"
		JSONObject json = ConnectUtils.doGetStr(ticketUrl_all)
		Map map = (Map)json
		resl.put("errcode", map.get("errcode"))
		String ticket = map.get("ticket")
		String signature = CheckUtils.getSignature4Ticket(timestamp, noncestr, ticket, url)
		
		resl.put("appId", appid)
		resl.put("timestamp", timestamp)
		resl.put("nonceStr", noncestr)
		resl.put("signature", signature)
		return resl
		
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
//			String userinfoUrl = userinfo+"?access_token="+token+"&openid="+openID+"&lang=zh_CN"
			//获取用户信息
//			String sTotalString1 =ConnectUtils.sendGet(userinfo,"access_token="+token+"&openid="+openID+"&lang=zh_CN")
//			
//			JSONObject json = JSONObject.fromObject(sTotalString1);
			
			String url = userinfo +"?access_token="+token+"&openid="+openID+"&lang=zh_CN"
//			String sTotalString1 =getJson(url)
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
	
	public boolean menuCreate(){
		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("weixinConfig.properties"))
		String createUrl = properties.getProperty("mp_menu_create")//创建自定义菜单URL 
		createUrl+="?access_token="+getAccessToken()
		String jsonStr = "{\"button\":[ {	\"type\":\"view\",\"name\":\"联系我们\",\"url\":\"http://mp.weixin.qq.com/s?__biz=MjM5MjIxOTY0OQ==&mid=201088048&idx=1&sn=4ae3aa0f943c5369ee5ac12f3b51a0b0#rd\"},"+
									"{	\"type\":\"view\",\"name\":\"微社区\",\"url\":\"http://m.wsq.qq.com/263238891\"}]}"
		
//		JSONObject json = JSONObject.fromObject(jsonStr);
//		println json
		JSONObject res = ConnectUtils.doPostStr(createUrl, jsonStr)
		return res.toString()
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
	
//	public static String getJson(String url) throws IOException, WeiboException{
//		
//		URL l_url = new URL(url);
//		HttpURLConnection l_connection = (HttpURLConnection) l_url.openConnection();
//		Response resp = new Response(l_connection);
//		return resp.asString();
//	}
}
