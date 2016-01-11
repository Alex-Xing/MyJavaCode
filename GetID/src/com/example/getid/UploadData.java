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
		// ����HttpPost����
		httpRequest = new HttpPost(url);
		try {
			// ����Http Request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// ȡ��Http Response
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			// ��״̬��Ϊ200
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// ��÷��ص�����
				//String strResult = EntityUtils.toString(httpResponse.getEntity());
				RS= "��Ϣ���ͳɹ�";

			} else {
				RS= ("���緵�ش��룺" + httpResponse.getStatusLine()
						.getStatusCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
			RS= (e.toString());
		}

	}

}
