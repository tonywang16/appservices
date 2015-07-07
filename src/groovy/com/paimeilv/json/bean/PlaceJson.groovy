package com.paimeilv.json.bean

import com.paimeilv.basic.Composite

class PlaceJson {
	public PlaceJson(com.paimeilv.basic.Place p){
		if(!p) return
		this.pid=p.id
		this.name=p.name
		this.instruction = p.instruction
		this.takeIndex=p.takeIndex
		this.address=p.address
		this.cover=p.cover
		this.longitude=p.longitude
		this.latitude=p.latitude
		
		this.autor = p.user.getFullname()
		this.autorPhoto = p.user.getPhoto()
		
		this.nearbySpot=p.nearbySpot
		this.nearbyPoint=p.nearbyPoint
		this.dress=p.dress
		this.takeStyle=p.takeStyle
		
		imglist= new ArrayList<Map>()
		Iterator i = p.composite.iterator()
		while (i.hasNext()) {
			Composite c = i.next()
			Map map = new HashMap()
			map.put("words", c.value)
			map.put("imgpath", c.image.pathstr)
			imglist.add(map)
		}
	}
	
	Long pid
	/** 名称 **/
	String name
	/** 副标题 */
	String instruction
	/** 拍摄指数 */
	Double  takeIndex
	/** 地址 **/
	String address
	/** 封面 ****/
	String cover
	/** 经度 ***/
	String longitude
	/** 纬度 ***/
	String latitude
	/** 创建人 **/
	String autor
	/*** 创建人头像 ***/
	String autorPhoto
	
	/** 附近景点 */
	String nearbySpot
	/** 附近趣处 */
	String nearbyPoint
	/** 推荐穿搭 */
	String dress
	/** 适拍风格 */
	String takeStyle
	
	/** 图片及描述集合 ****/
	List<Map> imglist
}
