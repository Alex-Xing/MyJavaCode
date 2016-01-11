package com.example.getid;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button MyBu = null;
	TextView ShowText = null;
	UploadData myupload = null;
	

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			// super.handleMessage(msg);
			ShowText(myupload.GetRS());
		}
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ShowText = (TextView) findViewById(R.id.textView1);
		ShowText.setText(GetAllInfo() + "\r\n" + GetInfoSignature());
		List<NameValuePair> params = CreatSendMsg();
		myupload = new UploadData(params,
				"http://192.168.1.100/Receive.php");
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				myupload.upload();
				Message message = new Message();
				message.what = 0;
				mHandler.sendMessage(message);
			}
		});
		thread.start();

		MyBu = (Button) findViewById(R.id.button1);
		MyBu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
		});
	}

	public void ShowText(String Text) {
		new AlertDialog.Builder(this).setTitle("操作提示").setMessage(Text)
				.setPositiveButton("确定", null).show();
	}

	public String GetAllInfo() {
		String ShowStr = "";
		ShowStr = GetModelName() + GetResolution() + Get_default_prop()
				+ Get_system_build_prop() + Get_system_default_prop()
				+ Get_data_local_prop() + Get_x86_prop()
				+ GetTelephonyManagerParam() + getSDTotalSize()
				+ getRomTotalSize();

		return ShowStr;
	}

	public String GetResolution() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.toString();
	}

	public String GetBrandName() {
		return android.os.Build.BRAND;
	}

	public String GetModelName() {
		return android.os.Build.MODEL;
	}

	public String GetInfoSignature() {
		return md5(GetAllInfo());
	}

	public String Get_default_prop() {
		String FileName = "/default.prop";
		if (!fileIsExists(FileName)) {
			return null;
		}
		return ReadTxtFile(FileName);
	}

	public String Get_system_build_prop() {
		String FileName = "/system/build.prop";
		if (!fileIsExists(FileName)) {
			return null;
		}
		return ReadTxtFile(FileName);
	}

	public String Get_system_default_prop() {
		String FileName = "/system/default.prop";
		if (!fileIsExists(FileName)) {
			return null;
		}
		return ReadTxtFile(FileName);
	}

	public String Get_data_local_prop() {
		String FileName = "/data/local.prop";
		if (!fileIsExists(FileName)) {
			return null;
		}
		return ReadTxtFile(FileName);
	}

	public String Get_x86_prop() {
		String FileName = "/x86.prop";
		if (!fileIsExists(FileName)) {
			return null;
		}
		return ReadTxtFile(FileName);
	}

	public String GetTelephonyManagerParam() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(TELEPHONY_SERVICE);
		String ShowStr = "";
		ShowStr = "getCallState=" + tm.getCallState() + "\r\n";

		// ShowStr=ShowStr+"电话方位："+tm.getCellLocation()+"\r\n";
		ShowStr = ShowStr + "getDeviceId=" + tm.getDeviceId() + "\r\n";
		ShowStr = ShowStr + "getDeviceSoftwareVersion="
				+ tm.getDeviceSoftwareVersion() + "\r\n";
		ShowStr = ShowStr + "getLine1Number=" + tm.getLine1Number() + "\r\n";

		// ShowStr=ShowStr+"附近的电话的信息："+tm.getNeighboringCellInfo().toString()+"\r\n";
		ShowStr = ShowStr + "getNetworkCountryIso=" + tm.getNetworkCountryIso()
				+ "\r\n";

		ShowStr = ShowStr + "getNetworkOperator=" + tm.getNetworkOperator()
				+ "\r\n";
		ShowStr = ShowStr + "getNetworkOperatorName="
				+ tm.getNetworkOperatorName() + "\r\n";

		ShowStr = ShowStr + "getNetworkType=" + tm.getNetworkType() + "\r\n";

		ShowStr = ShowStr + "getPhoneType=" + tm.getPhoneType() + "\r\n";

		ShowStr = ShowStr + "getSimCountryIso=" + tm.getSimCountryIso()
				+ "\r\n";

		ShowStr = ShowStr + "getSimOperator=" + tm.getSimOperator() + "\r\n";

		ShowStr = ShowStr + "getSimOperatorName=" + tm.getSimOperatorName()
				+ "\r\n";

		ShowStr = ShowStr + "getSimSerialNumber=" + tm.getSimSerialNumber()
				+ "\r\n";

		ShowStr = ShowStr + "getSimState=" + tm.getSimState() + "\r\n";

		ShowStr = ShowStr + "getSubscriberId=" + tm.getSubscriberId() + "\r\n";

		ShowStr = ShowStr + "getVoiceMailAlphaTag=" + tm.getVoiceMailAlphaTag()
				+ "\r\n";

		ShowStr = ShowStr + "getVoiceMailNumber=" + tm.getVoiceMailNumber()
				+ "\r\n";
		ShowStr = ShowStr + "hasIccCard=" + String.valueOf(tm.hasIccCard())
				+ "\r\n";
		ShowStr = ShowStr + "isNetworkRoaming="
				+ String.valueOf(tm.isNetworkRoaming()) + "\r\n";
		return ShowStr;
	}

	public String getRomTotalSize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		//blockSize = stat.getBlockSizeLong();
		long totalBlocks = stat.getBlockCount();
		//totalBlocks = stat.getTotalBytes();
		return Formatter.formatFileSize(MainActivity.this, blockSize
				* totalBlocks);
	}

	public String getSDTotalSize() {
		File path = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return Formatter.formatFileSize(MainActivity.this, blockSize
				* totalBlocks);
	}

	public List<NameValuePair> CreatSendMsg() {

		List<NameValuePair> params = null;
		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Signature", GetInfoSignature()));
		params.add(new BasicNameValuePair("Brand", GetBrandName()));
		params.add(new BasicNameValuePair("ModelName", GetModelName()));
		params.add(new BasicNameValuePair("Resolution", GetResolution()));
		params.add(new BasicNameValuePair("default_prop", Get_default_prop()));
		params.add(new BasicNameValuePair("system_build_prop",
				Get_system_build_prop()));
		params.add(new BasicNameValuePair("system_default_prop",
				Get_system_default_prop()));
		params.add(new BasicNameValuePair("data_local_prop",
				Get_data_local_prop()));
		params.add(new BasicNameValuePair("x86_prop", Get_x86_prop()));
		params.add(new BasicNameValuePair("TelephonyManagerParam",
				GetTelephonyManagerParam()));
		params.add(new BasicNameValuePair("RomTotalSize", getRomTotalSize()));
		params.add(new BasicNameValuePair("SDTotalSize", getSDTotalSize()));
		return params;
	}
	/*
	public void upload() {
		//String url = "http://199.193.248.141/ReceiveParam/Receive.php";
		String url = "http://192.168.1.100/Receive.php";
		HttpPost httpRequest = null;
		List<NameValuePair> params = null;
		HttpResponse httpResponse = null;
		// 建立HttpPost链接
		httpRequest = new HttpPost(url);
		// Post操作参数必须使用NameValuePair[]阵列储存

		params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("Signature", GetInfoSignature()));
		params.add(new BasicNameValuePair("Brand", GetBrandName()));
		params.add(new BasicNameValuePair("ModelName", GetModelName()));
		params.add(new BasicNameValuePair("Resolution", GetResolution()));
		params.add(new BasicNameValuePair("default_prop", Get_default_prop()));
		params.add(new BasicNameValuePair("system_build_prop",
				Get_system_build_prop()));
		params.add(new BasicNameValuePair("system_default_prop",
				Get_system_default_prop()));
		params.add(new BasicNameValuePair("data_local_prop",
				Get_data_local_prop()));
		params.add(new BasicNameValuePair("x86_prop", Get_x86_prop()));
		params.add(new BasicNameValuePair("TelephonyManagerParam",
				GetTelephonyManagerParam()));
		params.add(new BasicNameValuePair("RomTotalSize", getRomTotalSize()));
		params.add(new BasicNameValuePair("SDTotalSize", getSDTotalSize()));

		try {
			// 发送Http Request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得Http Response
			httpResponse = new DefaultHttpClient().execute(httpRequest);
			// 若状态码为200
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获得返回的数据
				//String strResult = EntityUtils.toString(httpResponse.getEntity());

			} else {
				// openDialog("Error!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	*/
	public boolean fileIsExists(String FileName) {
		File f = new File(FileName);
		if (!f.exists()) {
			return false;
		}
		return true;
	}

	public static String ReadTxtFile(String strFilePath) {
		String path = strFilePath;
		String content = ""; // 文件内容字符串
		// 打开文件
		File file = new File(path);
		// 如果path是传递过来的参数，可以做一个非目录的判断
		if (file.isDirectory()) {
			Log.d("TestFile", "The File doesn't not exist.");
		} else {
			try {
				InputStream instream = new FileInputStream(file);
				if (instream != null) {
					InputStreamReader inputreader = new InputStreamReader(
							instream);
					BufferedReader buffreader = new BufferedReader(inputreader);
					String line;
					// 分行读取
					while ((line = buffreader.readLine()) != null) {
						content += line + "\n";
					}
					instream.close();
				}
			} catch (java.io.FileNotFoundException e) {
				Log.d("TestFile", "The File doesn't not exist.");
			} catch (IOException e) {
				Log.d("TestFile", e.getMessage());
			}
		}
		return content;
	}

	public static String md5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}
}
