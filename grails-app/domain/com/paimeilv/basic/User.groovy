package com.paimeilv.basic

import com.paimeilv.config.RootFolder

class User implements Serializable {

	private static final long serialVersionUID = 1

	transient springSecurityService
	
	UserProfile userProfile//用户基本资料

	String username
	String password
	String email
	String telphone
	
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired
	
	Date dateCreated
	Date lastUpdated
	String loginBind = "0"
	int type = 0
	
	
	/** 头像 ***/
	public String getPhoto(){
		if(!this.userProfile?.userPhotoUrl||"".equals( this.userProfile?.userPhotoUrl)){//使用默认头像
			RootFolder rf =RootFolder.findByType("sys")
			return rf?.mappingPath+"default-avatar"
		}else if("1".equals( this.userProfile?.userPhotoUrl)){
			RootFolder rf =RootFolder.findByType("sys")
			return rf?.mappingPath+id+"-default-avatar"
		}else{
			return this.userProfile?.userPhotoUrl
		}
	}
	
	/** 昵称 ***/
	public String getFullname(){
		return this.userProfile?.fullName
	}
	/** 性别 ***/
	public String getGender(){
		if("W".equals(this.userProfile?.gender)) return "女"
		if(!this.userProfile?.gender) return "男"
	}
	/** 常驻地 ***/
	public String getLocation(){
		return this.userProfile?.location
	}

	/** 微信 ***/
	public Boolean getIsWeixin(){
		
		UserOpenID upid = UserOpenID.findWhere(user:this,openType:"weixin")
		
		if(upid) return true
		return false
	}
	
	/** 微博 ***/
	public Boolean getIsWeibo(){
		UserOpenID upid = UserOpenID.findWhere(user:this,openType:"weibo")
		
		if(upid) return true
		return false
	}
	
	public String getToken(String equipment){
		UserToken upid = UserToken.findWhere(user:this,equipment:equipment)
		
		return upid?.accessToken
	}
	User(String username, String password) {
		this()
		this.username = username
		this.password = password
	}

	@Override
	int hashCode() {
		username?.hashCode() ?: 0
	}

	@Override
	boolean equals(other) {
		is(other) || (other instanceof User && other.username == username)
	}

	@Override
	String toString() {
		username
	}

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static hasMany=[userOpenID:UserOpenID,image:Image,place:Place,placeTemp:PlaceTemp,message:Message,messageTo:MessageTo,praise:Praise,favorite:Favorite,comment:Comment,userToken:UserToken]
	
	static constraints = {
		username blank: false, unique: true
		password blank: false
		userProfile nullable:true
		
		telphone (nullable:true)
		email(email: true, unique: true,nullable:true)
	}

	static mapping = {
		password column: '`password`'
	}
}
