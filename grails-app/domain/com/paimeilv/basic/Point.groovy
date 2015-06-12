package com.paimeilv.basic

/** 趣处 **/
class Point {
	
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
	
	static belongsTo=[area:Area,user:User]
	
	static hasMany=[pointImage:PointImage,collect:Collect]

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
    }
}
