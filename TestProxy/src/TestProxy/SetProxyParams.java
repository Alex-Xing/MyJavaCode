package TestProxy;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

public class SetProxyParams {

	private Properties prop = System.getProperties();

	public void SetHttpProxy(String ProxyIP, String Prot) {
		// 设置http访问要使用的代理服务器的地址
		prop.setProperty("http.proxyHost", ProxyIP.trim());
		// 设置http访问要使用的代理服务器的端口
		prop.setProperty("http.proxyPort", Prot.trim());
	}

	public void SetHttpNonProxyHost(String HostList) {
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		// prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");
		prop.setProperty("http.nonProxyHosts", HostList);
	}

	public void SetHttpsProxy(String ProxyIP, String Prot) {
		// 设置安全访问使用的代理服务器地址与端口
		// 它没有https.nonProxyHosts属性，它按照http.nonProxyHosts 中设置的规则访问
		prop.setProperty("https.proxyHost", ProxyIP.trim());
		prop.setProperty("https.proxyPort", Prot.trim());
	}

	public void SetFtpProsy(String ProxyIP, String Prot) {
		// 使用ftp代理服务器的主机、端口以及不需要使用ftp代理服务器的主机
		prop.setProperty("ftp.proxyHost", ProxyIP.trim());
		prop.setProperty("ftp.proxyPort", Prot.trim());
	}

	public void SetFtpNonProxyHost(String HostList) {
		// 设置不需要通过代理服务器访问的主机，可以使用*通配符，多个地址用|分隔
		// prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		prop.setProperty("ftp.nonProxyHosts", HostList);
	}

	public void SetSocksProxy(String ProxyIP, String Prot) {
		// socks代理服务器的地址与端口
		prop.setProperty("socksProxyHost", ProxyIP.trim());
		prop.setProperty("socksProxyPort", Prot.trim());
	}

	public void SetProxyAuthenticator(String UserName, String Password) {
		Authenticator
				.setDefault(new ProxyAuthenticator(UserName, Password));
	}

	public static void main(String[] args) {
		Properties prop = System.getProperties();
		System.out.println(prop.getProperty("http.proxyHost"));
		System.out.println(prop.getProperty("http.proxyPort"));
		// TODO Auto-generated method stub
		SetProxyParams SetProxy = new SetProxyParams();
		SetProxy.SetHttpProxy("199.193.248.141", "3128");
		SetProxy.SetProxyAuthenticator("test", "951753");
		
		System.out.println(prop.getProperty("http.proxyHost"));
		System.out.println(prop.getProperty("http.proxyPort"));

		// 直接访问目的地址
		URL url;
		try {
			url = new URL("http://www.baidu.com");

			URLConnection con = url.openConnection();
			InputStreamReader isr = new InputStreamReader(con.getInputStream());
			char[] cs = new char[1024];
			int i = 0;
			while ((i = isr.read(cs)) > 0) {
				System.out.println(new String(cs, 0, i));
			}
			isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
