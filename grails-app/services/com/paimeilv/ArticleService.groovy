package com.paimeilv

import grails.transaction.Transactional

import com.paimeilv.basic.Article
import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Pageinate
import com.paimeilv.bean.Request

@Transactional
class ArticleService {

	/** 资讯列表 ****/
    def getArticleList(String pageno,String type) { 
		
		if(!type) type=""
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		List<Article> alist = Article.findAllByTypeLike("%${type}%",[max:page.pageSize, sort:"lastUpdated", order:"desc", offset:page.rowStart])
		page.rowCount =Article.countByTypeLike("%${type}%")
		List<com.paimeilv.json.bean.Article> ajsonlist = new ArrayList<com.paimeilv.json.bean.Article>()
		if(alist&&alist.size()>0){
			for (a in alist) {
				com.paimeilv.json.bean.Article aj = new com.paimeilv.json.bean.Article(a,null)
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
	
	/** 资讯详情 ****/
	def getArticle(Long aid,String accesstoken){
		Request req
		Article art = Article.get(aid)
		if(!art){
			req=new Request(false,"没有数据","error",null)
			return req
		}
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		req=new Request(true,"","success",new com.paimeilv.json.bean.Article(art,user))
		return req
	}
}
