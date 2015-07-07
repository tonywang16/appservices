package com.paimeilv

import grails.transaction.Transactional
import groovy.sql.Sql

import com.paimeilv.basic.Article
import com.paimeilv.basic.Circle
import com.paimeilv.basic.Place
import com.paimeilv.bean.Pageinate
import com.paimeilv.bean.Request
import com.paimeilv.json.bean.Site

@Transactional
class SearchService {
	def dataSource

    def search(String value,String type,String pageno) {

		Request req
		if(!type||"".equals(type.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		if("city".equals(type)){//地点
			return searchCity(value,page)
		}else if("article".equals(type)){//文章
			return searchArticle(value,page)
			
		}else if("food".equals(type)||"hotel".equals(type)||"shop".equals(type)||"view".equals(type)||"building".equals(type)||"pacilion".equals(type)){//趣处分类
			return searchPlace(value,page,type)
		}else{
			req=new Request(false,"请输入正确的搜索类型参数","error",null)
			return req
		}
    }
	
	/*** 地点搜索 ****/
	def searchCity(String value,Pageinate page){
		Request req
		List<Site> slist = new ArrayList<Site>()
		def sql = new Sql(dataSource);
		String strSql = "SELECT id,name,geared,type  FROM"+
								" (SELECT c.id,c.value as name,c.value as geared,c.search_index as search_index,'city' as type   FROM city c UNION "+
								" SELECT cr.id, cr.name as name,CONCAT(c1.value,',',cr.name) ,CONCAT(c1.search_index,',',cr.search_index) as search_index,'circle' as type FROM circle cr,city c1 where cr.city_id = c1.id UNION "+
								" SELECT p.id, p.name as name,CONCAT(c1.value,',',cr.name,',',p.name) ,CONCAT(c1.search_index,',',cr.search_index,',' ,p.name) as search_index,'place' as type FROM place p ,circle cr,city c1 where cr.city_id = c1.id and p.circle_id= cr.id)  "+
								" search where search_index like '%${value}%' order by geared limit "+page.rowStart+","+page.pageSize;
		sql.eachRow(strSql) {
			Site s = new Site(it.id,it.name, it.geared, it.type)
			slist.add(s)
		}
		String countsql = "SELECT count(1)  as  num FROM"+
								" (SELECT c.id,c.value as name,c.value as geared,c.search_index as search_index,'city' as type   FROM city c UNION "+
								" SELECT cr.id, cr.name as name,CONCAT(c1.value,',',cr.name) ,CONCAT(c1.search_index,',',cr.search_index) as search_index,'circle' as type FROM circle cr,city c1 where cr.city_id = c1.id UNION "+
								" SELECT p.id, p.name as name,CONCAT(c1.value,',',cr.name,',',p.name) ,CONCAT(c1.search_index,',',cr.search_index,',' ,p.name) as search_index,'place' as type FROM place p ,circle cr,city c1 where cr.city_id = c1.id and p.circle_id= cr.id)  "+
								" search where search_index like '%${value}%'";
		
		sql.eachRow(countsql) {num->
			page.rowCount = num.num
		}
		if(!slist||slist.size()==0){
			req=new Request(false,"","没有数据",page)
			return req
		}
		page.result = slist
		req=new Request(true,"","success",page)
		return req
	}
	
	/**资讯文章搜索***/
	def searchArticle(String value,Pageinate page){
		Request req
		List<com.paimeilv.json.bean.Article> ajsonlist = new ArrayList<com.paimeilv.json.bean.Article>()
		
		List<Article> alist = Article.findAllByTitleLike("%${value}%",[max:page.pageSize, sort:"title", order:"desc", offset:page.rowStart])

		page.rowCount =Article.countByTitleLike("%${value}%")
		if(alist&&alist.size()>0){
			for (a in alist) {
				com.paimeilv.json.bean.Article aj = new com.paimeilv.json.bean.Article(a)
				ajsonlist.add(aj)
			}
		}else{
			req=new Request(false,"","没有数据",page)
			return req
		}
		page.result = ajsonlist
		req=new Request(true,"","success",page)
		return req
	}
	
	/*** 趣处搜索 ***/
	def searchPlace(String value,Pageinate page,String type){
		List<Place> plist =  Place.executeQuery("select p from Place p ,PlaceType pt where pt.place=p and p.name like :value and pt.type =:type", [value:"%${value}%",type:type], [max:page.pageSize, offset:page.rowStart])
		Request req
		List<com.paimeilv.json.bean.Place> pjsonlist = new ArrayList<com.paimeilv.json.bean.Place>()
		if(plist&&plist.size()>0){
			for (p in plist) {
				com.paimeilv.json.bean.Place pj = new com.paimeilv.json.bean.Place(p)
				pjsonlist.add(pj)
			}
		}else{
			req=new Request(false,"","没有数据",page)
			return req
		}
		
		page.result = pjsonlist
		req=new Request(true,"","success",page)
		return req
	}
}
