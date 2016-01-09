package TestProxy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ProxyTestRobot implements Runnable {

	// 测试网页地址
	private String TargetURL = "http://www.ip.cn/";
	private String IP;
	private int Prot;
	private int TimeOut;
	private long ProxyTestSpeed = 0;
	private int ProxyTestWork = -1;
	private String Location = null;
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

	public void Init(String URL, String ProxyIP, int ProxyProt, // int
																// ProxyType,
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

	public int ProxyTestFunction(String URL, String ProxyIP, int ProxyProt, int ProxyType, int waittime) {

		try {
			URL url = new URL(URL.trim());
			// 创建代理服务器
			InetSocketAddress addr = new InetSocketAddress(ProxyIP.trim(), ProxyProt);
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
			String s = InputStreamToString(in, "UTF-8");
			in.close();
			return AnalyseHTML(s, ProxyIP.trim());

		} catch (Exception e) {
			// e.printStackTrace();

		} finally {

		}
		// System.out.println("ProxyTestEnd:");
		return -1;// 代理无效
	}

	// 分析测试网站返回的结果信息
	public int AnalyseHTML(String HTML, String ProxyIP) {
		//System.out.print(HTML);
		// 解析HTML内容
		DOMParser parser = new DOMParser();
		try {
			parser.setFeature("http://xml.org/sax/features/namespaces", false);//解決有命名空间的页面会报错的设置
			parser.parse(new InputSource(new ByteArrayInputStream(HTML.getBytes())));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// LoadPage((DOMParser) parser);
		Document doc = parser.getDocument();
		String xpath = "/html/body/div/div[4]/div/p[1]/code";
		String External_IP = queryByXpath(doc, xpath);
		xpath = "/html/body/div/div[4]/div/p[1]";
		Location = queryByXpath(doc, xpath);
		//System.out.print(External_IP + "：" + Location);

		int I;
		I = External_IP.indexOf(ProxyIP);
		//System.out.println("ProxyIP:" + ProxyIP);

		if (I > -1) {
			return 1;// 代理为匿名代理
		} else {
			return 0;// 代理为普通或透明代理
		}
	}

	// 获取指定XPATH节点的值
	public String queryByXpath(Document doc, String xpath) {
		String s = null;
		NodeList products;
		try {
			products = XPathAPI.selectNodeList(doc, xpath.toUpperCase());
			Node node = null;
			for (int i = 0; i < products.getLength(); i++) {
				node = products.item(i);
				s = node.getTextContent();
			}
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return s;
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
		TestResult = new String[6];

		// 每个IP测试两次，确认其代理类型
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
		TestResult[5] = Location;//.trim();
		ProcessStatus = false;

		//System.out.println("End:");
	}

	public void WaitTime(int S) {
		try {
			Thread.sleep(S);
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	public static String InputStreamToString(InputStream is, String charset) {
		ByteArrayOutputStream baos = null;

		try {
			baos = new ByteArrayOutputStream();
			int i = -1;
			while ((i = is.read()) != -1) {
				baos.write(i);
			}
			return baos.toString(charset);
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				baos = null;
			}
		}
		return null;
	}

	public String InputStreamTOString(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Auto-generated method stub
		ProxyTestRobot test = new ProxyTestRobot();
		//test.Init("http://www.ip.cn/", "47.88.28.12", 3128, "alex", "951753", 10);
		test.Init("http://www.ip.cn/", "120.52.73.22", 8080,  10);
		Thread T1 = new Thread(test);
		T1.start();
		System.out.println("Start:");

	}
}
