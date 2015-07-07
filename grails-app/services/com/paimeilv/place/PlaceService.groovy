package com.paimeilv.place

import grails.transaction.Transactional

import com.paimeilv.UserTokenUtils
import com.paimeilv.basic.Circle
import com.paimeilv.basic.City
import com.paimeilv.basic.Image
import com.paimeilv.basic.Place
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Pageinate
import com.paimeilv.bean.Request
import com.paimeilv.json.bean.CardJson
import com.paimeilv.json.bean.ImageJson
import com.paimeilv.json.bean.PlaceJson

@Transactional
class PlaceService {

	/** 城市列表 ***/
    def getCityList(String pageno,String value){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		if(!value) value = ""
		List<City> clist = City.findAllBySearchIndexLike("%${value}%",[max:page.pageSize,offset:page.rowStart])
		page.rowCount =City.countBySearchIndexLike("%${value}%")
		
		List<Map> rmlist = new ArrayList<Map>()
		
		if(clist&&clist.size()>0){
			for (c in clist) {
				Map map = new HashMap()
				map.put("cid", c.id)
				map.put("cvalue", c.value)
				rmlist.add(map)
			}
		}else{
			req=new Request(false,"","没有数据",page)
			return req
		}
		page.result = rmlist
		req=new Request(true,"","success",page)
		return req
    }
	
	/** 圈子列表 ***/
	def getCircleList(String pageno,String value,String cityId){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		if(!value) value = ""
		City city = null
		if(cityId&&!"".equals(cityId)){
			city = City.get(Long.valueOf(cityId))
			if(!city){
				req=new Request(false,"","没找到相应的城市",null)
				return req
			}
		}
		List<Circle> clist
		if(!city){
			clist =Circle.findAllBySearchIndexLike("%${value}%",[max:page.pageSize,offset:page.rowStart])
			page.rowCount =Circle.countBySearchIndexLike("%${value}%")
		}else{
			clist =Circle.findAll("from Circle c where c.searchIndex like :searchIndex and c.city = :city",[searchIndex:"%${value}%",city:city],[max:page.pageSize,offset:page.rowStart])
		}

		List<Map> rmlist = new ArrayList<Map>()
		
		if(clist&&clist.size()>0){
			for (c in clist) {
				Map map = new HashMap()
				map.put("cid", c.id)
				map.put("cvalue", c.name)
				rmlist.add(map)
			}
		}else{
			req=new Request(false,"","没有数据",page)
			return req
		}
		page.result = rmlist
		req=new Request(true,"","success",page)
		return req
	}
	
	def getPlaceList(String pageno,String value,String circleId){
		
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		if(!value) value = ""
		Circle cir = null
		if(circleId&&!"".equals(circleId)){
			cir = Circle.get(Long.valueOf(circleId))
			if(!cir){
				req=new Request(false,"","没找到相应的圈子",null)
				return req
			}
		}
		List<Place> plist
		if(!cir){
			plist =Place.findAllByNameLike("%${value}%",[max:page.pageSize,offset:page.rowStart])
			page.rowCount =Place.countByNameLike("%${value}%")
		}else{
			plist =Place.findAll("from Place p where p.name like :name and c.circle = :circle",[name:"%${value}%",circle:cir],[max:page.pageSize,offset:page.rowStart])
		}

		List<com.paimeilv.json.bean.Place> rmlist = new ArrayList<com.paimeilv.json.bean.Place>()
		
		if(plist&&plist.size()>0){
			for (p in plist) {
				com.paimeilv.json.bean.Place pjson = new com.paimeilv.json.bean.Place(p)
				rmlist.add(pjson)
			}
		}else{
			req=new Request(false,"","没有数据",page)
			return req
		}
		page.result = rmlist
		req=new Request(true,"","success",page)
		return req
	}
	
	
	/** 趣处详情 **/
	def getPlace(Long pid){
		Request req
		Place p = Place.get(pid)
		if(!p){
			req=new Request(false,"","没有数据",null)
			return req
		}
		req=new Request(true,"","success",new PlaceJson(p))
		return req
	}
	
	/****
	 * 获取分享的照片
	 * @param pId 趣处ID
	 * @param userId 用户ID
	 * @param pageno 页码
	 * @param orderby 排序方式
	 * @return
	 */
	def getPlaceImageList(Long pId,Long userId,String pageno,String orderby){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		
		List<ImageJson> ijlist = new ArrayList<ImageJson>()
		List ilist
		User user
		Place p
		String hql = "select i,(select count(1) from Favorite f where f.image=i) as fnum from Image i where 1=1 "
		if(pId&&0!=pId){
			p = Place.get(pId)
			if(!p) {
				req=new Request(false,"","没有找到趣处",null)
				return req
			}
			hql += " and i.card.place=:place "
			
		}else if(userId&&0!=userId){
			user = User.get(userId)
			if(!user) {
				req=new Request(false,"","没有找到用户",null)
				return req
			}
			hql += " and  i.user=:user "
		}else{
			req=new Request(false,"","userId,pId必须有一个有值",null)
			return req
		}
		
		if(orderby&&"hot".equals(orderby.toLowerCase().trim())){
			hql+="  order by fnum  desc"
		}else{
			hql+=" order by i.lastUpdated desc "
		}
		def param = new HashMap()
		if(p)param.put("place", p)
		if(user)param.put("user", user)
		ilist =  Image.executeQuery(hql, param,[max:page.pageSize,offset:page.rowStart])
		
		if(ilist&&ilist.size()>0){
			for (i in ilist) {
				ImageJson ij = new ImageJson(i[0])
				ijlist.add(ij)
			}
		}
		
		page.result = ijlist
		req=new Request(true,"","success",page)
		return req
	}
	
	def getCardList(Long pid,String pageno,String accesstoken){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		Place p
		if(pid&&0!=pid){
			p = Place.get(pid)
		}
		if(!p) {
			req=new Request(false,"","没有找到趣处",null)
			return req
		}
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String hql = "from Postcard pc where pc.place=:place and pc.comment is not null and pc.comment!=''"
		List<Postcard> pclist =  Postcard.findAll (hql,[place:p],[max:page.pageSize,offset:page.rowStart])
		List<CardJson> cjlist = new ArrayList<CardJson>()
		if(pclist&&pclist.size()>0){
			for (card in pclist) {
				CardJson cj = new CardJson(card,user)
				cjlist.add(cj)
			}
		}else{
			req=new Request(false,"","没有数据",null)
			return req
		}
		page.result = cjlist
		req=new Request(true,"","success",page)
		return req
	}
}
