package com.paimeilv

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.transaction.Transactional
import groovy.text.SimpleTemplateEngine

import java.text.SimpleDateFormat

import javax.servlet.http.HttpServletRequest

import com.paimeilv.basic.Role
import com.paimeilv.basic.User
import com.paimeilv.basic.UserProfile
import com.paimeilv.basic.UserRole
import com.paimeilv.config.Captcha

@Transactional
class RegisterService {
	
	def smsService
	
	def mailService

	/****email,手机注册**/
    def register(Map map) {
		String username = map.get("username")//用户名
		String password = map.get("password")//密码
		String captcha =  map.get("captcha")//验证码
		String regType = map.get("regType")//注册类型
		
		Map rmap = new HashMap()
		if(!username||"".equals(username.trim())||(!CheckUtil.checkTel(username)&&!CheckUtil.checkEmail(username))) {
			rmap.put("error", "请输入正确的手机号码或邮箱")
			return rmap
		}
		
		User newuser
		if(regType == "email"){//email注册
			User user = User.findByEmailOrUsername(username,username)
			if(user){
				rmap.put("error", "用户已存在")
				return rmap
			}
			newuser = new User(username:username,password:password,email:username,enabled:true).save(flush:true)
			
			UserProfile up = new UserProfile()
			up.user= user
			up.save(flush:true)
		  }
		  if(regType == "tel"){//手机注册
			  User user = User.findByUsernameOrTelphone(username,username)
			  if(user){
				  rmap.put("error", "用户已存在")
				  return rmap
			  }
			  
			  //TODO 验证码验证
			  Captcha c = Captcha.findByTelphone(username)
			  if(c&&!DateUtils.comparison2b(new Date(), c.lastUpdated)){  
//				    c?.delete()
			  }else{
			  		c?.delete()
					  rmap.put("error", "验证码已过期")
					  return rmap
			  }
			  
			  newuser = new User(username:username,password:password,telphone:username,enabled:true).save(flush:true)
			  
			  UserProfile up = new UserProfile()
			  up.user = newuser
			  up.save(flush:true)
			  println up
		  }
		  
		  //给注册者权限
		  def guestRole = Role.findByAuthority("ROLE_USER")//普通注册用户权限
		  UserRole.create(newuser, guestRole)
		  
		  rmap.put("user", newuser)
		  return rmap//注册成功
    }
	
	/**
	 * 获取验证码
	 * @param equip 设备号
	 * @return
	 */
	def getCaptcha(String telphone,String templateId,HttpServletRequest request,String codeMethod){
		
		User user = User.findByUsernameOrTelphone(telphone,telphone)
		
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
		Captcha c = Captcha.findByTelphone(telphone)
		
		if(!c){
			c = new Captcha()
		}
		c.telphone = telphone
		c.code= code
		c.save(flush:true)
		
		Map  map = smsService.templateSMS(telphone,code,templateId)
		String result = map.get("resp").get("respCode")
		Map m = new HashMap()
		if("000000".equals(result)) m.put("code", code)
		m.put("result", result)
		
		println("SMS Response content is: " + result);
		
		File file = new File(request.getSession().getServletContext().getRealPath("/log/register.txt"));
		String now =(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(Calendar.getInstance().getTime());
		result = "\r\n"+now +" "+telphone+ ":" + result+"\r\n"
		try {
			if (!file.exists()){
			file.createNewFile();
			}
			FileWriter writer = new FileWriter(request.getSession().getServletContext().getRealPath("/log/register.txt"), true);
			writer.write(result);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			println e.getMessage()
		}
		
		return m
	}
	
	//邮箱验证码
	def getEmailCode(String email,def config){
		User user = User.findWhere(email:email)
		User user1 = User.findWhere(username:email)
		if(!user || !user1){
			return "userNotExist"
		}
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
		//def config = ConfigurationHolder.config
		//def config = grailsApplication.config
		def body = config.grails.plugin.springsecurity.ui.identifyingCode.emailBody
		if (body.contains('$')) {
			body = evaluate(body, [user: user, param: param])
		}
		mailService.sendMail {
			to email
			from config.grails.mail.username
			subject config.grails.plugin.springsecurity.ui.identifyingCode.emailSubject
			html body.toString()
		}
		return param
	}
	
	protected String evaluate(s, binding) {
		new SimpleTemplateEngine().createTemplate(s).make(binding)
	}
	
	//修改密码
	def modifyPassword(Map map){
		String username = map.username
		String password = map.password
		String writeCode = map.writeCode
		String userCode = map.userCode
		User user = User.findWhere(username:username)
		if(!user){
			return "userNotExist"//用户不存在
		}
		 if(userCode != writeCode || !userCode.equals(writeCode)){
			  return "codeError"//验证码错误
		  }
		 user.password = password
		 user.save(flush:true)
		 return "success"//修改密码成功
	}
}
