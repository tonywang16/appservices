package com.paimeilv.json.bean.personal

import com.paimeilv.basic.MessageTo

class MessageToJson {

	public MessageToJson(MessageTo mt){
		this.form = mt.msg.from.fullname
		this.formPhoto = mt.msg.from.photo
		this.time = mt.sendtime
		this.type = mt.msg.type
		this.come = mt.msg.come
	}
	String form
	String formPhoto
	String time
	String type 
	String come
}
