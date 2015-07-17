package com.paimeilv

import grails.transaction.Transactional

import com.paimeilv.basic.Article
import com.paimeilv.basic.Comment
import com.paimeilv.basic.Composite
import com.paimeilv.basic.Image
import com.paimeilv.basic.Place
import com.paimeilv.basic.PlaceTemp
import com.paimeilv.basic.Postcard
import com.paimeilv.basic.User
import com.paimeilv.basic.UserToken
import com.paimeilv.bean.Request
import com.paimeilv.config.RootFolder

@Transactional
class OperateService {
	
	def qiNiuService

	/** 发现趣处 ***/
	def proposalPlace(Map place,String accesstoken){
		Request req
		if(!place||!place?.pname||"".equals(!place?.pname?.trim())||!place?.paddress||"".equals(!place?.paddress?.trim())||!accesstoken||"".equals(!accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String pname = place?.pname //趣处名称
		String paddress = place?.paddress //趣处地址
		String profile = place?.profile //趣处简介
		String pimage = place?.pimage //趣处图片 多图以 "|"分割
		
		String pid = place?.pId
		PlaceTemp pt
		if(pid&&!"".equals(pid)){
			pt = PlaceTemp.get(Long.valueOf(pid))
		}
		
		if(!pt) pt = new PlaceTemp()
		pt.name = pname
		pt.address = paddress
		pt.instruction = profile
		pt.user = user
		pt.save(flush:true)
		String[] imgarray = pimage.split("\\|")
		List<Map> keyList = getImgArray(imgarray)
		List<Composite> comlist = Composite.findAllWhere(pointTemp:pt,user:user)
		
		if(comlist&&comlist.size()>0){
			for(int i =0;i<comlist.size();i++){
				
				Composite composite = comlist.get(i)
				Image img = composite.image
				boolean isdel = true
				for(int j =0;j<keyList.size();j++){
					String key = keyList.get(j).get("key")
					if(key.equals(img.path)){//已有图片修改
						isdel = false
						keyList.remove(j)
						j--
						break
					}
				}
				if(isdel){
					//删除操作
					qiNiuService.del(img.path,img.rootfolder.bucket)
					composite.delete(flush:true)
					img.delete(flush:true)
					comlist.remove(i)
					i--
				}
			}
		}
		
		if(keyList&&keyList.size()>0){
			//新添加图片
			for(int j =0;j<keyList.size();j++){
				String key = keyList.get(j).get("key")
				String bucket = keyList.get(j).get("bucket")
				///获取根目录
				RootFolder rf =RootFolder.findWhere(bucket:bucket)
				if(!rf) {
					rf =  new RootFolder(bucket:bucket)
					rf.qiniuPath = "http://"+bucket+".qiniudn.com/"
					rf.save(flush:true)
				}
				Composite composite =Composite.findWhere(pointTemp:pt)//图片添加
				if(!composite){
					Image img = new Image()
					img.rootfolder = rf
					img.path = key
					img.user = user
					img.name = key
					img.save(flush:true)
					composite =new Composite()
					composite.image = img 
					composite.pointTemp = pt
					composite.save(flush:true)
					img.composite = composite
					img.save(flush:true)
				}
			}
		}
		req=new Request(true,"","success",null)
		return req
	}
	
	/** 修改或保存趣处评分 ***/
	def saveOrUpdateCard(Map card,String accesstoken){
		
		Request req
		if(!card||!card?.pid||"".equals(card.pid.trim())||!card.score||"".equals(card.score.trim())||!accesstoken||"".equals(!accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		
		String pid =card.pid //趣处ID
		String score = card.score //趣处评分
		String comment = card.comment //趣处评论
		String image = card.image //趣处评分分享图片 多图以 "|"分割
		
		
		Long placeId = Long.valueOf(pid)
		Double s = Double.valueOf(score)
		
		Place place = Place.get(placeId)
		
		if(!place){
			req=new Request(false,"趣处找不到，或已删除","error",null)
			return req
		}
		Postcard pc = Postcard.findWhere(user:user,place:place)

		if(!pc) pc = new Postcard()
		pc.place=place
		pc.user=user
		pc.comment = comment
		pc.score = s
		pc.save(flush:true)
		
		String[] imgarray = image.split("\\|")
		
		List<Map> keyList = getImgArray(imgarray)
		List<Image> imglist = Image.findAllWhere(card:pc,user:user)
		
		if(imglist&&imglist.size()>0){
			for(int i =0;i<imglist.size();i++){
				Image img = imglist.get(i)
				boolean isdel = true
				for(int j =0;j<keyList.size();j++){
					String key = keyList.get(j).get("key")
					if(key.equals(img.path)){//已有图片修改
						isdel = false
						keyList.remove(j)
						j--
						break
					}
				}
				if(isdel){
					//删除操作
					qiNiuService.del(img.path,img.rootfolder.bucket)
					img.delete(flush:true)
					imglist.remove(i)
					i--
				}
			}
		}
		
		if(keyList&&keyList.size()>0){
			//新添加图片
			for(int j =0;j<keyList.size();j++){
				String key = keyList.get(j).get("key")
				String bucket = keyList.get(j).get("bucket")
				///获取根目录
				RootFolder rf =RootFolder.findWhere(bucket:bucket)
				if(!rf) {
					rf =  new RootFolder(bucket:bucket)
					rf.qiniuPath = "http://"+bucket+".qiniudn.com/"
					rf.save(flush:true)
				}
				Image img =Image.findWhere(path:key,name:key,card:pc,user:user)//图片添加
				if(!img){
					img = new Image(path:key,name:key,card:pc,rootfolder:rf,user:user)
					img.save(flush:true)
				}else{
					img.save(flush:true)
				}
			}
		}
		
		req=new Request(true,"","success",null)
		return req
	}
	
	/** 获取到上传的图片集合 ****/
	def getImgArray(imgPath){
		
		List<Map> keyList = new ArrayList<Map>()
		
		if(imgPath.getClass().isArray()){//判断传进参数是否是数组
			for(int i=0;i<imgPath.length;i++){
				String path = imgPath[i]
				if(path&&!"null".equals(path)){
					String  a1 = path.split("//")[1]
					String bucket = a1.substring(0,a1.indexOf("."))
					String key = a1.substring(a1.indexOf("/")+1,a1.length())
					
					Image img = Image.findWhere(path:key)
					
					Map map = new HashMap()
					if(!img){
						if(qiNiuService.move(bucket, QiNiuService.UPLOAD_BUCKET, key)){
							map = new HashMap()
							map.put("key", key)
							map.put("bucket", QiNiuService.UPLOAD_BUCKET)
						}
					}else{
						map = new HashMap()
						map.put("key", img.path)
						map.put("bucket", img.rootfolder.bucket)
					}
					if(map) keyList.add(map)
				}
			}
		}else if(imgPath&&!"".equals(imgPath)){
			String  a1 = imgPath.split("//")[1]
			String bucket = a1.substring(0,a1.indexOf("."))
			
			String key = a1.substring(a1.indexOf("/")+1,a1.length())
			
			Image img = Image.findWhere(path:key)
			
			Map map
			if(!img){
				if(qiNiuService.move(bucket, QiNiuService.UPLOAD_BUCKET, key)){
					map = new HashMap()
					map.put("key", key)
					map.put("bucket", QiNiuService.UPLOAD_BUCKET)
				}
			}else{
				map = new HashMap()
				map.put("key", img.path)
				map.put("bucket", img.rootfolder.bucket)
			}
			if(map) keyList.add(map)
		}
		return keyList
	}
	
	/** 评论图片 ****/
	def commImage(Integer imgId,String content,Integer commId,String accesstoken){
		
		Request req
		if(!imgId||!content||"".equals(content.trim())||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		if(content.length()>250){
			req=new Request(false,"评论字符不能超过250字","error",null)
			return req
		}
		
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		
		
		Image image = Image.get(imgId)
		if(!image){
			req=new Request(false,"没找到要评论的图片","error",null)
			return req
		}
		
		
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		Comment co = new Comment()
		co.strText= content
		co.user = user
		co.image = image
		if(commId){
			Comment comm=Comment.get(commId)
			if(!comm){
				req=new Request(false,"没找到要回复的评论","error",null)
				return req
			}
			co.image=comm.image
			co.parent = comm
		}
		co.save(flush:true)
		
		req=new Request(true,"","success",null)
		return req
	}
	
	
	/** 评论资讯 ****/
	def commArticle(Integer aId,String content,Integer commId,String accesstoken){
		
		Request req
		if(!aId||!content||"".equals(content.trim())||!accesstoken||"".equals(accesstoken.trim())){
			req=new Request(false,"参数错误","error",null)
			return req
		}
		if(content.length()>250){
			req=new Request(false,"评论字符不能超过250字","error",null)
			return req
		}
		
		Map cm = UserTokenUtils.checkUserToken(accesstoken)
		if(!cm.get("result")){
			req=new Request(cm.get("result"),cm.get("msg"),"error",null)
			return req
		}
		
		
		Article article = Article.get(aId)
		if(!article){
			req=new Request(false,"没找到要评论的文章","error",null)
			return req
		}
		
		
		UserToken ut = (UserToken)cm.get("userToken")
		User user = ut?.user
		Comment co = new Comment()
		co.strText= content
		co.user = user
		co.article = article
		if(commId){
			Comment comm=Comment.get(commId)
			if(!comm){
				req=new Request(false,"没找到要回复的评论","error",null)
				return req
			}
			co.article=comm.article
			co.parent = comm
		}
		co.save(flush:true)
		
		req=new Request(true,"","success",null)
		return req
	}
}
