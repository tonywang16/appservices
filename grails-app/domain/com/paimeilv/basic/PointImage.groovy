package com.paimeilv.basic

class PointImage {
	
	/*** 描述 **/
	String value 
	
	Image image 
	
	static belongsTo=[point:Point,pointTemp:PointTemp]

    static constraints = {
		
		point nullable: true
		pointTemp nullable: true
		value nullable: true
    }
}
