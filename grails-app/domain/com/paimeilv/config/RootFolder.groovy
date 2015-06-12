package com.paimeilv.config

import com.paimeilv.basic.Image


/**
 * 图片根路径
 */
class RootFolder {

	/** 七牛空间路径 **/
	String qiniuPath
	/** 映射路径 **/
	String mappingPath
	/** 七牛空间 **/
	String bucket
	static hasMany		= [image:Image]	// tells GORM to associate other domain objects for a 1-n or n-m mapping
	
	/** 目录类型(img:用户上传图片的路径，sys:系统配置图片的路径) ***/
	String type ="img"
	
	
	
    static mapping = {
		version false
    }
    
	static constraints = {
		mappingPath nullable: true
		bucket nullable: true
    }
	
	/*
	 * Methods of the Domain Class
	 */
//	@Override	// Override toString for a nicer / more descriptive UI 
//	public String toString() {
//		return "${name}";
//	}
}
