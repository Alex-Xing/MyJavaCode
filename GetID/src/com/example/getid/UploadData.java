package com.example.getid;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

public class UploadData {
	List<NameValuePair> params = null;
	String url = "http://192.168.1.100/Receive.php";
	String RS="";
	public UploadData(List<NameValuePair> pa, String URL) {
		params = pa;
		url = URL;
	}
	public String GetRS(){
		return RS;
	}
	public void upload() {

		HttpPost httpRequest = null;

		HttpResponse httpResponse = null;
		// 建立HttpPost链接
		httpRequest = new HttpPost(url);
		try {
			// 发送Http Request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得Http Response
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			// 若状态码为200
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获得返回的数据
				//String strResult = EntityUtils.toString(httpResponse.getEntity());
				RS= "信息发送成功";

			} else {
				RS= ("网络返回代码：" + httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			RS= (e.toString());
		}

	}

}
