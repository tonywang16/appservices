package com.paimeilv.basic

/** 临时趣处表 ****/
class PointTemp {
	
	/** 名称 **/
	String name
	
	/** 地址 **/
	String address
	
	/** 说明 */
	String instruction
	
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
	
	/** 城市 ***/
	String city
	
	/** 圈子 ***/
	String area
	
	static belongsTo=[user:User]
	static hasMany=[pointImage:PointImage]

    static constraints = {
		address nullable: true
		instruction nullable: true
		takeIndex nullable: true
		nearbySpot nullable: true
		nearbyPoint nullable: true
		dress nullable: true
		takeStyle nullable: true
		city nullable: true
		area nullable: true
    }
}
