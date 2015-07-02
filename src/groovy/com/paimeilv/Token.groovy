package com.paimeilv

import com.paimeilv.basic.User

class Token implements Serializable{
	private static final long serialVersionUID = 1
	
	   String value
	   User user
	
	   String toString() {
		  " $value"
	   }
}
