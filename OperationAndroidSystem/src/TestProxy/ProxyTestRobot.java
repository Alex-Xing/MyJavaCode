package TestProxy;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

public class ProxyTestRobot implements Runnable {

	private String TargetURL = "http://phppro.500yun.pw/ip.php";
	private String IP;
	private int Prot;
	private int TimeOut;
	private long ProxyTestSpeed = 0;
	private int ProxyTestWork = -1;
	// private String Name;
	// private String Passwd;
	private boolean ProcessStatus = true;
	private String[] TestResult = null;

	public String[] GetTestResult() {
		return TestResult;
	}

	public boolean GetProcessStatus() {
		return ProcessStatus;
	}

	public void Init(String URL, String ProxyIP, int ProxyProt,// int ProxyType,
			String UserName, String Password, int WaitTime) {
		TargetURL = URL.trim();
		IP = ProxyIP.trim();
		Prot = ProxyProt;
		TimeOut = WaitTime;
		// Name = UserName;
		// Passwd = Password;
		Authenticator.setDefault(new ProxyAuthenticator(UserName, Password));
		// ServerType=ProxyType;
	}

	public void Init(String URL, String ProxyIP, int ProxyProt, int WaitTime) {
		TargetURL = URL.trim();
		IP = ProxyIP.trim();
		Prot = ProxyProt;
		TimeOut = WaitTime;
	}

	public int ProxyTestFunction(String URL, String ProxyIP, int ProxyProt,
			int ProxyType, int waittime) {

		try {
			URL url = new URL(URL.trim());
			// 创建代理服务器
			InetSocketAddress addr = new InetSocketAddress(ProxyIP.trim(),
					ProxyProt);
			Proxy proxy = null;
			switch (ProxyType) {
			case 0:
				proxy = new Proxy(Proxy.Type.DIRECT, addr); // 不用 代理
				break;
			case 1:
				proxy = new Proxy(Proxy.Type.HTTP, addr); // HTTP 代理
				break;
			case 2:
				proxy = new Proxy(Proxy.Type.SOCKS, addr); // SOCKS 代理
				break;
			}

			// 如果我们知道代理server的名字, 可以直接使用
			// 结束

			URLConnection conn = url.openConnection(proxy);
			conn.setConnectTimeout(waittime * 1000);
			conn.setReadTimeout(waittime * 1000);
			InputStream in = conn.getInputStream();
			String s = InputStreamTOString(in, "UTF-8");//IOUtils.toString(in, "UTF-8");
			int I;
			I = s.indexOf(ProxyIP);
			in.close();
			System.out.println("ProxyIP:" + ProxyIP);
			System.out.println(s);
			if (I > -1) {
				return 1;// 代理为匿名代理
			} else {
				return 0;// 代理为普通或透明代理
			}
		} catch (Exception e) {
			// e.printStackTrace();

		} finally {

		}
		// System.out.println("ProxyTestEnd:");
		return -1;// 代理无效
	}

	public boolean ProxyTest(int Type) {
		Date begin = new Date();
		ProxyTestWork = ProxyTestFunction(TargetURL, IP, Prot, Type, TimeOut);
		Date end = new Date();
		ProxyTestSpeed = (end.getTime() - begin.getTime());// / 1000;
		// System.out.println(ProxyTestSpeed);

		boolean Result = false;
		if (ProxyTestWork == -1) {
			Result = false;
		} else {
			Result = true;
		}
		return Result;
	}

	public void run() {
		// TODO Auto-generated method stub
		ProcessStatus = true;
		TestResult = new String[5];

		if (ProxyTest(1)) {
			TestResult[4] = "HTTP";

		} else if (ProxyTest(2)) {
			TestResult[4] = "SOCKS";

		} else {
			TestResult[4] = null;
		}
		TestResult[0] = IP.trim();
		TestResult[1] = String.valueOf(Prot).trim();
		TestResult[2] = String.valueOf(ProxyTestSpeed).trim();
		TestResult[3] = String.valueOf(ProxyTestWork).trim();
		ProcessStatus = false;

		// System.out.println("End:");
	}

	public void WaitTime(int S) {
		try {
			Thread.sleep(S);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public String InputStreamTOString(InputStream in, String encoding) {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int BUFFER_SIZE = 4096;
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		try {
			while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
				outStream.write(data, 0, count);
			data = null;
			return new String(outStream.toByteArray(), encoding);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		ProxyTestRobot test = new ProxyTestRobot();
		test.Init("http://phppro.500yun.pw/ip.php", "199.193.248.141", 3128,
				"test", "951753", 10); 
		test.run();
//		Thread T1 = new Thread(test);
//		T1.start();
		System.out.println("Start:");

	}
}
