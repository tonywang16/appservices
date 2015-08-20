package com.paimeilv

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.transaction.Transactional
import groovy.text.SimpleTemplateEngine

import javax.servlet.http.HttpServletRequest

import com.paimeilv.basic.Role
import com.paimeilv.basic.User
import com.paimeilv.basic.UserProfile
import com.paimeilv.basic.UserRole
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Request
import com.paimeilv.config.Captcha

@Transactional
class RegisterService {
	
	def smsService
	
	def mailService
	
	def qiNiuService

	/****email,手机注册**/
    def register(Map map) {
		String username = map.get("username")//用户名
		String password = map.get("password")//密码
		String captcha =  map.get("captcha")//验证码
		String regType = map.get("regType")//注册类型
		String equip = map.get("equip")
		
		Map rmap = new HashMap()
		if(!username||"".equals(username.trim())||(!CheckUtil.checkTel(username)&&!CheckUtil.checkEmail(username))) {
			rmap.put("error", "请输入正确的手机号码或邮箱")
			return rmap
		}
		
		if(!equip||"".equals(equip)){
			rmap.put("error", "设备号不能为null")
			return rmap
		}
		
		User newuser
		User user = User.findByEmailOrUsernameOrTelphone(username,username,username)
		if(user){
			rmap.put("error", "用户已存在")
			return rmap
		}
		//TODO 验证码验证
		Captcha c = Captcha.findByTelphoneAndCode(username,captcha)
		if(c){
			if(DateUtils.comparison2b(new Date(), c.lastUpdated)){
				c?.delete()
				rmap.put("error", "验证码已过期")
				return rmap
			   }
		}else{
				rmap.put("error", "验证码不存在")
				return rmap
		}
		if(regType == "email"){//email注册
			newuser = new User(username:username,password:password,email:username,enabled:true).save(flush:true)
			UserProfile up = new UserProfile()
			up.user= newuser
			up.save(flush:true)
			
		  }
		  if(regType == "tel"){//手机注册
			  newuser = new User(username:username,password:password,telphone:username,enabled:true).save(flush:true)
			  UserProfile up = new UserProfile()
			  up.user = newuser
			  up.save(flush:true)
		  }
		  
		  //给注册者权限
		  def guestRole = Role.findByAuthority("ROLE_USER")//普通注册用户权限
		  UserRole.create(newuser, guestRole)
		  
		  /*** 创建Token ****/
		  UserTokenUtils.getUserToken(equip, newuser)
		  
		  com.paimeilv.json.bean.User u = new com.paimeilv.json.bean.User(newuser,equip)
		  
		  rmap.put("user", u)
		  return rmap//注册成功
    }
	
	/**
	 * 获取验证码
	 * @param equip 设备号
	 * @return
	 */
	def getCaptcha(String username,String templateId,HttpServletRequest request,def config,String codeMethod){
		
		User user = User.findByUsernameOrTelphoneOrEmail(username,username,username)
		
		Map rmap = new HashMap()
		if(codeMethod == "register"){
			if(user){
				rmap.put("error", "用户已存在")
				return rmap
			}
		}
		if(codeMethod == "reset"){//忘记密码
			if(!user){
				rmap.put("error", "用户不存在")
				return rmap
			}
		}
		
		int n = 0 ;
		while(n < 100000){
			n = (int)(Math.random()*1000000);
		}
		String code=n
		Captcha c = Captcha.findByTelphone(username)
		
		if(!c){
			c = new Captcha()
		}
		c.telphone = username
		c.code= code
		c.save(flush:true)
		
		Map m = new HashMap()
		
		if(CheckUtil.checkTel(username)){
			Map  map = smsService.templateSMS(username,code,templateId)
			String result = map.get("resp").get("respCode")
			
//			if("000000".equals(result)) m.put("code", code)
			m.put("result", result)
			
			println("SMS Response content is: " + result);
			
//			File file = new File(request.getSession().getServletContext().getRealPath("/log/register.txt"));
//			String now =(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
//			result = "\r\n"+now +" "+username+ ":" + result+"\r\n"
//			try {
//				if (!file.exists()){
//				file.createNewFile();
//				}
//				FileWriter writer = new FileWriter(request.getSession().getServletContext().getRealPath("/log/register.txt"), true);
//				writer.write(result);
//				writer.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				println e.getMessage()
//			}
		}else{
			m=getEmailCode(user,username,config)
		}
		
		return m
	}
	
	//邮箱验证码
	def getEmailCode(User user,String email,config){
		String username = email
		if(user) username = user.fullname
		
		int n = 0 ;
		while(n < 100000){
			n = (int)(Math.random()*1000000);
		}
		String code=n
		Captcha c = Captcha.findByTelphone(email)
		
		if(!c){
			c = new Captcha()
		}
		c.telphone = email
		c.code= code
		c.save(flush:true)
		String param = code
		def conf = SpringSecurityUtils.securityConfig
		def body = config.grails.plugin.springsecurity.ui.identifyingCode.emailBody
		if (body.contains('$')) {
			body = evaluate(body, [username:username, param: param])
		}
		mailService.sendMail {
			to email
			from conf.ui.register.emailFrom
			subject config.grails.plugin.springsecurity.ui.identifyingCode.emailSubject
			html body.toString()
		}
		
		Map m = new HashMap()
//		m.put("code", param)
		m.put("result", "000000")
		return m
	}
	
	protected String evaluate(s, binding) {
		new SimpleTemplateEngine().createTemplate(s).make(binding)
	}
	
	//修改密码
	def modifyPassword(Map map){
		String username = map.username
		String newpsw = map.newpsw
		String captcha = map.captcha
		
		Map  capmap = checkCaptcha(username,captcha)
		Map m = new HashMap()
		if(!capmap.get("result")){
			m.put("error", capmap.get("msg"))
			return m
		}
		
		User user = User.findByUsernameOrTelphoneOrEmail(username,username,username)
		if(!user){
			m.put("error", "用户不存在")
			return m
		}
		
		 user.password = newpsw
		 user.save(flush:true)
		 
		 m.put("success", "已重置")
		 return m//修改密码成功
	}
	
	def checkCaptcha(String username,String code){
		Captcha cap = Captcha.findByTelphoneAndCode(username,code)
		Map map = new HashMap()
		
		
		if(!cap) {
			map.put("result",false)
			map.put("msg","验证码错误")
			return map
		}
		
		Date now = new Date()
		Map dmap = DateUtils.comparison(now, cap.lastUpdated)
		Long dd = dmap.get("d"); //天数
		Long hh = dmap.get("h");//小时
		Long mm = dmap.get("m"); //分钟
		Long ss = dmap.get("s"); //秒
		if(dd<1&&hh<1){
			map.put("result",true)
		}else{
			map.put("result",false)
			map.put("msg","验证码失效")
		}
		return map
	}
	
	/*** 修改用户信息，包括修改密码与修改头像 ***/
	def updateUser(Map userParams,String accesstoken){
		Request req
		if(!userParams||!userParams?.name||"".equals(userParams?.name?.trim())||!userParams?.gander||"".equals(userParams?.gander?.trim())||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		if(!"M".equals(userParams?.gander?.trim())&&!"W".equals(userParams?.gander?.trim())){
			req=new Request(false,"性别参数错误(M:男,W:女)","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String name = userParams?.name?.trim() //趣处名称
		String photo =userParams?.photo?.trim() //趣处地址
		String gander = userParams?.gander?.trim() //趣处简介
		String location = userParams?.location?.trim() //趣处图片 多图以 "|"分割
		String password = userParams?.password?.trim() //趣处名称
		String restpsw = userParams?.restpsw?.trim() //趣处名称
		
		UserProfile userp = user.userProfile
		if(!userp) {
			userp = new UserProfile()
			userp.user = user
			userp.save(flush:true)
		}
		if(!name.equals(userp.fullName)){
			UserProfile up = UserProfile.findByFullName(name)
			if(up) return new Request(false,"昵称已存在","error",null)
		}
		if(photo&&!"".equals(photo)){//修改头像
			String  a1 = photo.split("//")[1]
			String bucket = a1.substring(0,a1.indexOf("."))
			String key = a1.substring(a1.indexOf("/")+1,a1.length())
			if(qiNiuService.replace(bucket, QiNiuService.UPLOAD_BUCKET, key, user.id+"-app-default-avatar")){
				userp.userPhotoUrl = "1"
				userp.save(flush:true)
			}else{
				return new Request(false,"修改头像失败","error",null)
			}
		}
		
		userp.fullName = name
		userp.gender = gander
		userp.location = location
		userp.save(flush:true)
		
		req=new Request(true,"","success",null)
		return req
	}
}
