package com.paimeilv

import org.apache.commons.httpclient.util.DateUtil
import net.sf.json.JSONObject;

import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.BasicHttpEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.apache.log4j.Logger
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.support.PropertiesLoaderUtils

import com.google.gson.Gson
import com.sms.DateUtil
import com.sms.EncryptUtil
import com.sms.TemplateSMS

/**
 * SmsService
 * A service class encapsulates the core business logic of a Grails application
 */
class SmsService{
	static transactional = true
	public static String ip="api.ucpaas.com";
	public static String version="2014-06-30";
	private static Logger logger=Logger.getLogger(SmsService.class);
	public Map templateSMS(String to, String param,String templateId) {

		def properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("config.properties"))

		String accountSid = properties.getProperty("sms_accountSid")
		String authToken = properties.getProperty("sms_authToken")
		String appId = properties.getProperty("sms_appId")


		// TODO Auto-generated method stub
		String result = "";
		JSONObject jsonObject
		DefaultHttpClient httpclient=getDefaultHttpClient();
		try {
			//MD5加密
			EncryptUtil encryptUtil = new EncryptUtil();
			// 构造请求URL内容
			String timestamp = DateUtil.dateToStr(new Date(),
					DateUtil.DATE_TIME_NO_SLASH);// 时间戳
			String signature =getSignature(accountSid,authToken,timestamp,encryptUtil);
			String url = getStringBuffer().append("/").append(version)
					.append("/Accounts/").append(accountSid)
					.append("/Messages/templateSMS")
					.append("?sig=").append(signature).toString();
			TemplateSMS templateSMS=new TemplateSMS();
			templateSMS.setAppId(appId);
			templateSMS.setTemplateId(templateId);
			templateSMS.setTo(to);
			templateSMS.setParam(param);
			Gson gson = new Gson();
			String body = gson.toJson(templateSMS);
			body="{\"templateSMS\":"+body+"}";
			logger.info(body);
			HttpResponse response=post("application/json",accountSid, authToken, timestamp, url, httpclient, encryptUtil, body);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				result = EntityUtils.toString(entity, "UTF-8");
				
				jsonObject = JSONObject.fromObject(result);
			}
			EntityUtils.consume(entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			// 关闭连接
			httpclient.getConnectionManager().shutdown();
		}
		return (Map)jsonObject;
	}

	public DefaultHttpClient getDefaultHttpClient(){
		DefaultHttpClient httpclient=new DefaultHttpClient();
		return httpclient;
	}
	public String getSignature(String accountSid, String authToken,String timestamp,EncryptUtil encryptUtil) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	public StringBuffer getStringBuffer() {
		StringBuffer sb = new StringBuffer("https://");
		sb.append(ip);
		return sb;
	}
	public HttpResponse post(String cType,String accountSid,String authToken,String timestamp,String url,DefaultHttpClient httpclient,EncryptUtil encryptUtil,String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
		requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
		requestBody.setContentLength(body.getBytes("UTF-8").length);
		httppost.setEntity(requestBody);
		// 执行客户端请求
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
}
