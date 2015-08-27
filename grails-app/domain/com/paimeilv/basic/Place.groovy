package com.paimeilv.basic

import java.util.Date;

import com.paimeilv.config.RootFolder

/** 趣处 **/
class Place {
	
	/** 名称 **/
	String name
	
	/** 副标题 */
	String instruction
	
	/** 地址 **/
	String address
	
	/** 拍摄指数 */
	Double  takeIndex
	
	/** 附近景点 */
	String nearbySpot 
	
	/** 附近趣处 */
	String nearbyPoint
	
	/** 推荐穿搭 */
	String dress
	
	/** 适拍风格 */
	String takeStyle
	
	/** 经度 ***/
	String longitude
	
	/** 纬度 ***/
	String latitude
	
	String tel
	
	boolean isCover=false 
	
	boolean isSend=false
	
	Date lastUpdated
	
	/** 封面 ***/
	public String getCover(){
		if(!isCover){//使用默认头像
			RootFolder rf =RootFolder.findByType("sys")
			return rf?.mappingPath+"default-place-cover"
		}else{
			RootFolder rf =RootFolder.findByType("sys")
			return rf?.mappingPath+id+"-default-place-cover"
		}
	}
	
	public String getDescribe(){
		this.circle?.describe+","+this.name
	}
	
	static belongsTo=[circle:Circle,user:User]
	
	static hasMany=[composite:Composite,favorite:Favorite,postcard:Postcard]

	static mapping = {
		version false
	}
    static constraints = {
		address nullable: true
		instruction nullable: true
		takeIndex nullable: true
		nearbySpot nullable: true
		nearbyPoint nullable: true
		dress nullable: true
		takeStyle nullable: true
		isCover nullable: true
		
		longitude nullable: true
		latitude nullable: true
		
		tel nullable: true
    }
}
