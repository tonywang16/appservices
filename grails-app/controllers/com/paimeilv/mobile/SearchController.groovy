package com.paimeilv.mobile

import grails.converters.JSON

/** 搜索 ****/
class SearchController {

	def searchService
    def index() {
		//value=厦门&type=city&pageno=2
		String value = params.get("value")
		String type = params.get("type")
		String pageno = params.get("pageno")		
		
		render searchService.search(value,type,pageno) as JSON
	}
}
