package com.paimeilv.basic

import java.util.Date;

import com.paimeilv.config.RootFolder;

/** 图片 *****/
class Image {
	
	String path
	String name
	
	Date lastUpdated
	RootFolder rootfolder
	
	static belongsTo=[pointImage:PointImage,user:User,card:Card]
	static hasMany=[collect:Collect,comment:Comment]
    static constraints = {
		rootfolder nullable: true
		name nullable: true
		card nullable: true
		pointImage nullable: true
		user nullable: true
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
