package com.paimeilv.mobile

import com.xiaomi.xmpush.server.Constants
import com.xiaomi.xmpush.server.Message

/** 推送 **/
class PushController {

    def index() {
		Constants.useSandbox();
	}
	
	private Message buildMessage() throws Exception {
		String description = "notification description";
		Message message = new Message.IOSBuilder()
				.description(description)
				.soundURL("default")    // 消息铃声
				.badge(1)               // 数字角标
				.category("action")     // 快速回复类别
				.extra("key", "value")  // 自定义键值对
				.build();
		return message;
   }
}
