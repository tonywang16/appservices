package com.paimeilv.basic


/**
 * UserProfile
 * 用户资料
 */
class UserProfile {
	String fullName //全名
	Date dateOfBirth //生日
	String gender = "M" //性别
	String note//个人说明（个性签名）
	
	String location//常驻地
	
	/* Automatic timestamping of GORM */
	Date dateCreated
	Date lastUpdated
	
	String userPhotoUrl="" //头像
	
	static belongsTo=[user:User]
	
    static constraints = {
		fullName(nullable: true)
		note nullable: true
		dateOfBirth(nullable: true)
		gender(inList: ["M", "W"])
		location nullable:true
		
//		equipment  nullable:true
//		interest nullable:true
//		style nullable:true
    }
	/** tostring 方法*/
	String toString() {
		if(!fullName){
			return ""
		}
		return "${fullName}"
	}
}
