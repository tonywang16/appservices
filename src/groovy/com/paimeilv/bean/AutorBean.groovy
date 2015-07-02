package com.paimeilv.bean

class AutorBean {
	String email
	String photo
	String username
	String gender
	String openId
	String accessToken
	String openType
	int isexist =0 //是否已注册 ------ 0：未注册，1：已注册 
	int userType = 0
	
	int state =0 //状态 是否请求成功 ---0：请求成功，-1：请求失败
}
