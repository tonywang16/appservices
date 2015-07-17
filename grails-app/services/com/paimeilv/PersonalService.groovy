package com.paimeilv

import grails.transaction.Transactional

import com.paimeilv.basic.Article
import com.paimeilv.basic.Favorite
import com.paimeilv.basic.Image
import com.paimeilv.basic.MessageTo
import com.paimeilv.basic.Place
import com.paimeilv.basic.PlaceTemp
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.User
import com.paimeilv.basic.UserProfile
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Pageinate
import com.paimeilv.bean.Request
import com.paimeilv.json.bean.ImageJson
import com.paimeilv.json.bean.personal.CardJson
import com.paimeilv.json.bean.personal.MessageToJson
import com.paimeilv.json.bean.personal.PlaceTempJson

@Transactional
class PersonalService {
	
	def qiNiuService

	/** 趣处的评论列表 ***/
	def getCardList(String pageno,String accesstoken){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String hql = "from Postcard pc where pc.user=:user "
		List<Postcard> pclist =  Postcard.findAll (hql,[user:user],[max:page.pageSize,offset:page.rowStart])
		List<CardJson> cjlist = new ArrayList<CardJson>()
		if(pclist&&pclist.size()>0){
			for (card in pclist) {
				CardJson cj = new CardJson(card)
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
	
	/** 获取发现的趣处列表 **/
	def getProposalPlaceList(String pageno,String accesstoken){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		List<PlaceTemp> ptlist = PlaceTemp.findAllWhere(user:user,[max:page.pageSize,offset:page.rowStart])
		
		List<PlaceTempJson> ptjlist = new ArrayList<PlaceTempJson>()
		if(ptlist&&ptlist.size()>0){
			for (pt in ptlist) {
				PlaceTempJson ptj = new PlaceTempJson(pt)
				ptjlist.add(ptj)
			}
		}else{
			req=new Request(false,"","没有数据",null)
			return req
		}
		page.result = ptjlist
		req=new Request(true,"","success",page)
		return req
	}
	
	def getProposalPlace(String pId,String accesstoken){
		
		Request req
		if(!pId||"".equals(pId.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		PlaceTemp pt = PlaceTemp.get(Long.valueOf(pId))
		
		if(pt){
			req=new Request(true,"","success",new PlaceTempJson(pt))
			return req
		}else{
			req=new Request(false,"没有数据","error",null)
			return req
		}
	}
	
	/*** 获取收藏列表 ***/
	def getFavoriteList(String type,String accesstoken,String pageno){
		Request req
		if(!type||"".equals(type.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		if("image".equals(type)){
			List<Image> imglist =  Image.executeQuery("select i from Image i ,Favorite f where f.image = i and f.user=:user", [user:user],[max:page.pageSize,offset:page.rowStart])
			
			List<ImageJson> ijlist = new ArrayList<ImageJson>()
			if(imglist&&imglist.size()>0){
				for (i in imglist) {
					ImageJson ij = new ImageJson(i[0])
					ijlist.add(ij)
				}
				
				page.result = ijlist
			}else{
				req=new Request(false,"没有数据","error",null)
				return req
			}
			
		}else if("article".equals(type)){
			List<Article> articlelist =  Article.executeQuery("select a from Article a ,Favorite f where f.article =a and f.user=:user", [user:user],[max:page.pageSize,offset:page.rowStart])
			List<com.paimeilv.json.bean.Article> ajsonlist = new ArrayList<com.paimeilv.json.bean.Article>()
			if(articlelist&&articlelist.size()>0){
				for (a in articlelist) {
					com.paimeilv.json.bean.Article aj = new com.paimeilv.json.bean.Article(a,user)
					ajsonlist.add(aj)
				}
				page.result = ajsonlist
			}else{
				req=new Request(false,"","没有数据",page)
				return req
			}
		}else if("place".equals(type)){
			List<Place> plist =  Place.executeQuery("select p from Place p ,Favorite f where f.place = p and f.user=:user", [user:user],[max:page.pageSize,offset:page.rowStart])
			List<com.paimeilv.json.bean.Place> ajsonlist = new ArrayList<com.paimeilv.json.bean.Place>()
			if(plist&&plist.size()>0){
				for (p in plist) {
					com.paimeilv.json.bean.Place pjson = new com.paimeilv.json.bean.Place(p)
					ajsonlist.add(pjson)
				}
				page.result = ajsonlist
			}else{
				req=new Request(false,"","没有数据",page)
				return req
			}
		}else if("card".equals(type)){
			List<Postcard> pclist =  Postcard.executeQuery("select c from Postcard c ,Favorite f where f.card = c and f.user=:user", [user:user],[max:page.pageSize,offset:page.rowStart])
			List<com.paimeilv.json.bean.CardJson> cjlist = new ArrayList<com.paimeilv.json.bean.CardJson>()
			if(pclist&&pclist.size()>0){
				for (card in pclist) {
					com.paimeilv.json.bean.CardJson cj = new com.paimeilv.json.bean.CardJson(card,user)
					cjlist.add(cj)
				}
			}else{
				req=new Request(false,"","没有数据",null)
				return req
			}
			page.result = cjlist
		}else{
			req=new Request(false,"请输入正确的收藏类型参数","error",null)
			return req
		}
		
		req=new Request(true,"","success",page)
		return req
	}
	
	def getUser(String accesstoken){
		Request req
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		if(!user){
			return new Request(false,"没有数据","error","")
		}
		req=new Request(true,"","success",new com.paimeilv.json.bean.User(user,null))
		return req
	}
	
	/*** 修改用户信息，包括修改密码与修改头像 ***/
	def updateUser(Map userParams,String accesstoken){
		Request req
		if(!userParams||!userParams?.name||"".equals(userParams?.name?.trim())||!userParams?.gander||"".equals(userParams?.gander?.trim())||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		if(!"M".equals(userParams?.gander?.trim())&&!"W".equals(userParams?.gander?.trim())){
			req=new Request(false,"性别参数错误(M:男,W:女)","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String name = userParams?.name?.trim() //趣处名称
		String photo =userParams?.photo?.trim() //趣处地址
		String gander = userParams?.gander?.trim() //趣处简介
		String location = userParams?.location?.trim() //趣处图片 多图以 "|"分割
		String password = userParams?.password?.trim() //趣处名称
		String restpsw = userParams?.restpsw?.trim() //趣处名称
		
		UserProfile userp = user.userProfile
		if(!userp) {
			userp = new UserProfile()
			userp.user = user
			userp.save(flush:true)
		}
		if(!name.equals(userp.fullName)){
			UserProfile up = UserProfile.findByFullName(name)
			if(up) return new Request(false,"昵称已存在","error",null)
		}
		if(photo&&!"".equals(photo)){//修改头像
			String  a1 = photo.split("//")[1]
			String bucket = a1.substring(0,a1.indexOf("."))
			String key = a1.substring(a1.indexOf("/")+1,a1.length())
			if(qiNiuService.replace(bucket, QiNiuService.UPLOAD_BUCKET, key, user.id+"-app-default-avatar")){
				userp.userPhotoUrl = "1"
				userp.save(flush:true)
			}else{
				return new Request(false,"修改头像失败","error",null)
			}
		}
		
		if(password&&!"".equals(password)){//修改密码
			if(password.length() < 7){
				req=new Request(false,"密码长度不能小于7!","error",null)
				return req
			}else if(!password.equals(restpsw)){
				req=new Request(false,"确认密码不一致","error",null)
				return req
			}else{
				user.setPassword(password)
				user.save(flush:true)
			}
		}
		
		userp.fullName = name
		userp.gender = gander
		userp.location = location
		userp.save(flush:true)
		
		req=new Request(true,"","success",null)
		return req
	}
	/** 用户所拥有的收藏数量、订单数量、趣处评分及发现的趣处的数量 */
	def getUserAmount(String accesstoken){
		Request req
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		if(!user){
			return new Request(false,"用户找不到","error","")
		}
		Map map = new  HashMap()
		map.put("order", 0)//TODO 订单数
		Integer pcNum =  user.card.size()?:0
		Integer ptNum =  user.placeTemp.size()?:0
		
		List<Integer>  fplace = Favorite.executeQuery("select count(1) from Favorite f where f.user=:user and f.place is not null ",[user:user])
		List<Integer>  fcard = Favorite.executeQuery("select count(1) from Favorite f where f.user=:user and f.card is not null ",[user:user])
		List<Integer>  fimage = Favorite.executeQuery("select count(1) from Favorite f where f.user=:user and f.image is not null ",[user:user])
		List<Integer>  farticle = Favorite.executeQuery("select count(1) from Favorite f where f.user=:user and f.article is not null ",[user:user])
		
		map.put("card",pcNum)//趣处评分
		map.put("proposalPlace",ptNum)//发现的趣处
		map.put("fplace",fplace.get(0))//收藏趣处
		map.put("fcard",fcard.get(0))//收藏趣处评分
		map.put("fimage",fimage.get(0))//收藏图片
		map.put("farticle",farticle.get(0))//收藏文章
		req=new Request(true,"","success",map)
		return req
	}
	
	/** 获取发现的趣处列表 **/
	def getMessageList(String pageno,String accesstoken){
		Request req
		Pageinate page = new Pageinate()
		if(pageno&&!"".equals(pageno)) page.pagesNo = Integer.valueOf(pageno)
		
		/** 验证并获取登录用户 ***/
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		List<MessageTo> mtlist = MessageTo.findAllWhere(to:user,[max:page.pageSize,sort:"dateCreated", order:"desc",offset:page.rowStart])
		
		List<MessageToJson> mtjlist = new ArrayList<MessageToJson>()
		if(mtlist&&mtlist.size()>0){
			for (mt in mtlist) {
				MessageToJson ptj = new MessageToJson(mt)
				mtjlist.add(ptj)
			}
		}else{
			req=new Request(false,"","没有数据",null)
			return req
		}
		page.result = mtjlist
		req=new Request(true,"","success",page)
		return req
	}
}
