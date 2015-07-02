package com.paimeilv.basic

/** 文章 ***/
class Article {

	/**标题**/
	String title
	
	/** 类型 **/
	String type 
	
	/** 内容 **/
	String content
	
	static hasMany=[favorite:Favorite,comment:Comment]
	static belongsTo=[user:User]
	
    static constraints = {
    }
}
