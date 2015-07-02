package com.paimeilv.json.bean

class User {
	
	public User(com.paimeilv.basic.User user,String equipment){
		
		if(!user) return 
		this.userId = user.id
		this.fullname = user.getFullname()
		this.photo = user.getPhoto()
		this.gender = user.getGender()
		this.location = user.getLocation()
		this.username =  user?.username
		this.isweixin =  user.getIsWeixin()
		this.isweibo =  user.getIsWeibo()
		if(equipment&&!"".equals(equipment.trim())){
			this.token = user.getToken(equipment)
		}
	}
	
	Long userId 
	String fullname
	
	String photo
	
	String gender
	
	String location
	
	String username
	
	Boolean isweixin
	
	Boolean isweibo
	
	String token

}
