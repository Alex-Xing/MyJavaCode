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
		// ����http����Ҫʹ�õĴ���������ĵ�ַ
		prop.setProperty("http.proxyHost", ProxyIP.trim());
		// ����http����Ҫʹ�õĴ���������Ķ˿�
		prop.setProperty("http.proxyPort", Prot.trim());
	}

	public void SetHttpNonProxyHost(String HostList) {
		// ���ò���Ҫͨ��������������ʵ�����������ʹ��*ͨ����������ַ��|�ָ�
		// prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");
		prop.setProperty("http.nonProxyHosts", HostList);
	}

	public void SetHttpsProxy(String ProxyIP, String Prot) {
		// ���ð�ȫ����ʹ�õĴ����������ַ��˿�
		// ��û��https.nonProxyHosts���ԣ�������http.nonProxyHosts �����õĹ������
		prop.setProperty("https.proxyHost", ProxyIP.trim());
		prop.setProperty("https.proxyPort", Prot.trim());
	}

	public void SetFtpProsy(String ProxyIP, String Prot) {
		// ʹ��ftp������������������˿��Լ�����Ҫʹ��ftp���������������
		prop.setProperty("ftp.proxyHost", ProxyIP.trim());
		prop.setProperty("ftp.proxyPort", Prot.trim());
	}

	public void SetFtpNonProxyHost(String HostList) {
		// ���ò���Ҫͨ��������������ʵ�����������ʹ��*ͨ����������ַ��|�ָ�
		// prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		prop.setProperty("ftp.nonProxyHosts", HostList);
	}

	public void SetSocksProxy(String ProxyIP, String Prot) {
		// socks����������ĵ�ַ��˿�
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

		// ֱ�ӷ���Ŀ�ĵ�ַ
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
