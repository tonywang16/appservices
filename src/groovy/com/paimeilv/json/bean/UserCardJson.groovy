package com.paimeilv.json.bean

import com.paimeilv.basic.Image
import com.paimeilv.basic.Postcard

class UserCardJson {

	public UserCardJson(Postcard c){
		if(!c) return
		this.cardId=c.id
		this.score =c.score
		this.comment =c.comment
		this.pId = c.place.id
		
		imglist= new ArrayList<String>()
		Iterator i= c.image.iterator()
		while (i.hasNext()) {
			Image img = i.next()
			imglist.add(img.pathstr)
		}
	}
	
	
	Long pId
	
	Long cardId
	/** 评分 */
	Double  score
	/** 评论 */
	String comment
	
	/** 图片路径 ***/
	List<String> imglist
	
}
