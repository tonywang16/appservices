package com.paimeilv.basic

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

	static hasMany=[userOpenID:UserOpenID,image:Image,point:Point,pointTemp:PointTemp,message:Message,messageTo:MessageTo,praise:Praise,collect:Collect,comment:Comment]
	
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
