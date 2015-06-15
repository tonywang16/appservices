package com.paimeilv.basic

class Composite {
	
	/*** 描述 **/
	String value 
	
	Image image 
	
	static belongsTo=[point:Place,pointTemp:PlaceTemp]

    static constraints = {
		
		point nullable: true
		pointTemp nullable: true
		value nullable: true
    }
}
