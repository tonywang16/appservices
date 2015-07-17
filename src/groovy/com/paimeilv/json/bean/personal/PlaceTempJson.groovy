package com.paimeilv.json.bean.personal

import com.paimeilv.basic.Composite
import com.paimeilv.basic.PlaceTemp

class PlaceTempJson {

	public PlaceTempJson(PlaceTemp pt){
		this.pId = pt.id
		this.name = pt.name
		this.address = pt.address
		this.instruction = pt.instruction
		image =  new ArrayList<Map>()
		
		Iterator i = pt.composite.iterator()
		while (i.hasNext()) {
			Composite c = i.next()
			Map map = new HashMap()
			map.put("words", c.value)
			map.put("imgpath", c.image.pathstr)
			image.add(map)
		}
	}
	Long pId
	String name
	String address
	
	String instruction
	
	List<Map> image
}
