package com.paimeilv

//import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser
//import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService
//import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.userdetails.GrailsUser
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException

import com.paimeilv.basic.User


class CustomUserDetailsService implements GrailsUserDetailsService {
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]
	
	public static final String USERNAME_LOGINID_SPLIT = "/";
	
	GrailsApplication grailsApplication
 
    UserDetails loadUserByUsername(String username, boolean loadRoles) 
    throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }
 
    UserDetails loadUserByUsername(String username) 
    throws UsernameNotFoundException {
	
        User.withTransaction { status ->
			
			String[] args  = username.split(USERNAME_LOGINID_SPLIT);
			
			username= args[0];//用户名
			String equip = args[1];//设备号
			
            User user =User.findByUsernameOrEmailOrTelphone(username, username,username)
            if (!user)
                throw new UsernameNotFoundException('User not found', username)
				
			/*** 创建Token ****/	
//			grailsApplication.mainContext.getBean("tokenService").getUserToken(equip,user)
			UserTokenUtils.getUserToken(equip, user)
			
            def authorities = user.authorities.collect {
                new GrantedAuthorityImpl(it.authority)}
			def guser
			if("1".equals(user.loginBind)){
				String password=user.password
				password = user.encodePassword(password)
				guser= new GrailsUser(user.username,password, user.enabled,
				!user.accountExpired, !user.passwordExpired, !user.accountLocked,
					authorities ?: NO_ROLES, user.id)
			}else{
				guser = new GrailsUser(user.username, user.password, user.enabled, 
	            !user.accountExpired, !user.passwordExpired, !user.accountLocked,
	                authorities ?: NO_ROLES, user.id)
			}
			return guser
        }
    }
	}