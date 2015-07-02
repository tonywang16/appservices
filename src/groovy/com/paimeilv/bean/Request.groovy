package com.paimeilv.bean

class Request {
	
	public Request(boolean result,String exception,String msg,Object data){
		this.result = result
		this.exception = exception
		this.msg = msg
		this.data = data
	}
	boolean result
	
	String exception
	
	String msg
	
	Object data
}
