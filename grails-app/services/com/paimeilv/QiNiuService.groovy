package com.paimeilv

import org.springframework.web.multipart.commons.CommonsMultipartFile

import com.paimeilv.config.RootFolder
import com.qiniu.api.auth.digest.Mac
import com.qiniu.api.io.IoApi
import com.qiniu.api.io.PutExtra
import com.qiniu.api.io.PutRet
import com.qiniu.api.net.CallRet
import com.qiniu.api.rs.Entry
import com.qiniu.api.rs.PutPolicy
import com.qiniu.api.rs.RSClient

class QiNiuService {
	
	def springSecurityService

	public static  String ACCESS_KEY  = "eOcnURMZUwJlMhO7HVpYjYe5W28LCHEpp-5_ylt-"
	public static  String SECRET_KEY = "2ZuJyp8w0b0yEpfw6wE1CPzi7gj6XszI8QDmuiui"
	public static  String UPLOAD_BUCKET="paimeilvupload"
	public static  String TEMP_BUCKET="pmltemp"
	QiNiuService(){
		
	}
	def upload(CommonsMultipartFile f,int type,String bucketName){
		return upload(f.inputStream,type,bucketName)
	}
	
	/*** 判断空间上是否有该文件 ****/
	def getFile(String bucketName,String key){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		RSClient client = new RSClient(mac);
		Entry statRet = client.stat(bucketName,key);
		if(statRet?.getFsize()&&statRet.getFsize()>0){
			return false
		}else{
			return true
		}
	}
	
	/** 自定义名称 */
	def upload(InputStream inputStream,String bucketName,String key){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		PutPolicy putPolicy = new PutPolicy(bucketName);
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		PutRet ret = IoApi.putStream(uptoken, key, inputStream, extra);
		if(!ret.getKey()||"null".equals(ret.getKey())) return null
		RootFolder rf = RootFolder.findWhere(bucket:bucketName)
		if(rf){
			return rf.mappingPath+ret.getKey()
		}
		 return "http://"+bucketName+".qiniudn.com/"+ret.getKey()
	}
	
	def upload(InputStream inputStream,int type,String bucketName){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		PutPolicy putPolicy = new PutPolicy(bucketName);
		String uptoken = putPolicy.token(mac);
		PutExtra extra = new PutExtra();
		String key = String.valueOf(System.currentTimeMillis())
		
		if(springSecurityService.isLoggedIn()){
				String uid = String.valueOf(springSecurityService.principal.id)
				key=uid+"_"+key
		}
		PutRet ret = IoApi.putStream(uptoken, key, inputStream, extra);
		if(!ret.getKey()||"null".equals(ret.getKey())) return null
		
		if(0==type){ 
			RootFolder rf = RootFolder.findWhere(bucket:bucketName)
			if(rf){
				return rf.mappingPath+ret.getKey()
			}
			return "http://"+bucketName+".qiniudn.com/"+ret.getKey()
		}
		
		return ret.getKey()
	}
	
	def del(String path,String bucketName){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		RSClient client = new RSClient(mac);
		String key = path.substring(path.lastIndexOf("/")+1,path.length())
		CallRet ret = client.delete(bucketName, key);
		if(200==ret.getStatusCode()) return true
		else return false
	}
	
	def move(String bucketSrc,String bucketDest,String key){
		move(bucketSrc,bucketDest,key,key)
	}
	
	def move(String bucketSrc,String bucketDest,String keySrc,String keyDest){
		if(bucketSrc.equals(bucketDest)) return true
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		RSClient client = new RSClient(mac);
		CallRet ret = client.move(bucketSrc,keySrc, bucketDest, keyDest);
		if(200==ret.getStatusCode()||614==ret.getStatusCode()) return true
		else return false
	}
	
	/**
	 *  移动文件，替换
	 * @param bucketSrc 被转移空间
	 * @param bucketDest 转移到的空间
	 * @param keySrc 被转移文件
	 * @param keyDest 转移到文件
	 * @return
	 */
	def replace(String bucketSrc,String bucketDest,String keySrc,String keyDest){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		RSClient client = new RSClient(mac);
		Entry statRet = client.stat(bucketDest,keyDest);
		if(statRet?.getFsize()&&statRet.getFsize()>0){
			CallRet ret = client.delete(bucketDest, keyDest); //删除
		}
		return move(bucketSrc,bucketDest,keySrc,keyDest)
	}
	
	def uptoken(String bucket){
		Mac mac = new Mac(ACCESS_KEY, SECRET_KEY);
		// 请确保该bucket已经存在
		PutPolicy putPolicy = new PutPolicy(bucket);
		String uptoken = putPolicy.token(mac);
		return uptoken
	}
	
}
