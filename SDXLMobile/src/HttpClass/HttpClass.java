package HttpClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpClass {

	private String GetHttpResponseStr = null;// 返回的字符串
	private HttpResponse Response = null;// 返回的操作状态参数
	private HttpEntity ReturnEntity = null;// 返回的对像实例
	private DefaultHttpClient httpclient = new DefaultHttpClient();

	public String GetHttpResponseStr() {
		return GetHttpResponseStr;
	}

	public HttpResponse GetHttpResponse() {
		return Response;
	}

	public HttpEntity GetReturnEntity() {
		return ReturnEntity;
	}

	public void DoGet(String URL) throws Exception {
		GetHttpResponseStr = null;// 返回的字符串
		Response = null;// 返回的操作状态参数
		ReturnEntity = null;// 返回的对像实例

		HttpGet httpGet = new HttpGet(URL);
		httpGet.setHeader("User-Agent", "神雕侠侣 1.1.1 (iPhone; iPhone OS 6.1.4; zh_CN)"); 
		httpGet.setHeader("Connection","keep-alive"); 		
		httpGet.setHeader("Accept-Encoding","gzip");//

		Response = httpclient.execute(httpGet);
		try {
			ReturnEntity = Response.getEntity();
			GetHttpResponseStr = EntityUtils.toString(ReturnEntity);
			byte[] bs = EntityUtils.toByteArray(ReturnEntity);
 
			EntityUtils.consume(ReturnEntity);
			System.out.println(changeCharset(GetHttpResponseStr, "GBK"));
		} finally {
			httpGet.releaseConnection();
		}
	}

	public void DoPost() throws Exception {
		GetHttpResponseStr = null;// 返回的字符串
		Response = null;// 返回的操作状态参数
		ReturnEntity = null;// 返回的对像实例
		HttpPost httpPost = new HttpPost("http://targethost/login");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("username", "vip"));
		nvps.add(new BasicNameValuePair("password", "secret"));
		httpPost.setEntity(new UrlEncodedFormEntity(nvps));

		HttpResponse response2 = httpclient.execute(httpPost);

		try {
			System.out.println(response2.getStatusLine());
			HttpEntity entity2 = response2.getEntity();
			// do something useful with the response body
			// and ensure it is fully consumed
			EntityUtils.consume(entity2);
		} finally {
			httpPost.releaseConnection();
		}
	}
	
	 public  String changeCharset(String str, String newCharset)  throws  UnsupportedEncodingException  {
		 if(str != null) {
	            //用默认字符编码解码字符串。与系统相关，中文windows默认为GB2312
	            byte[] bs = str.getBytes();
	            
	            return new String(bs, newCharset);            }
	        return null;
	 }

}
