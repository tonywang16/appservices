package com.paimeilv.basic

import java.text.SimpleDateFormat

import com.paimeilv.DateUtils
import com.paimeilv.config.RootFolder

/** 图片 *****/
class Image {
	
	String path
	String name
	
	Date lastUpdated
	RootFolder rootfolder
	
	/** 发布设置 ***/
	public String getSendtime(){
		return DateUtils.getSendTime(lastUpdated)
	}
	
	static belongsTo=[composite:Composite,user:User,card:Postcard,article:Article]
	static hasMany=[favorite:Favorite,comment:Comment,praise:Praise]
    static constraints = {
		rootfolder nullable: true
		name nullable: true
		card nullable: true
		composite nullable: true
		user nullable: true
		article nullable: true
    }
	
	String getPathstr(){
		String str = path
		if(rootfolder){
				if(rootfolder.mappingPath&&!"".equals(rootfolder.mappingPath)){
					str = rootfolder.mappingPath+path
				}else{
					str = rootfolder.qiniuPath+path
				}
			}
		
		return str
	}
}
