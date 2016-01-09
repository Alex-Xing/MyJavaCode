package GetPRoxyIPRobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import AnalyseWebPage.AnalyseProxy360;
import AnalyseWebPage.AnalyseXICI;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class BasicCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	private String[] myCrawlDomains;

	@Override
	public void onStart() {
		myCrawlDomains = (String[]) myController.getCustomData();
	}

	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();
		//System.out.println("Test URL: " + url);
		if (FILTERS.matcher(href).matches()) {
			return false;
		}
		for (String crawlDomain : myCrawlDomains) {
			if (href.startsWith(crawlDomain)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void visit(Page page) {
		/*
		 * int docid = page.getWebURL().getDocid();
		 * 
		 * int parentDocid = page.getWebURL().getParentDocid();
		 * 
		 * System.out.println("Docid: " + docid); System.out.println("URL: " +
		 * url); System.out.println("Docid of parent page: " + parentDocid);
		 */
		//System.out.println(page.toString());
		if (page.getParseData() instanceof HtmlParseData) {
			String url = page.getWebURL().getURL();
			String Domain = page.getWebURL().getDomain();
			//String[] str =(String[]) myController.getCustomData();
			//URL TestURL = new URL(str[0]);

			System.out.println("URL: " + url);
			//System.out.println("Domain: " + Domain);
			//System.out.println("=============");
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String html = htmlParseData.getHtml();
			//System.out.println(html);
			//生成最终获取的网页分析真实的XPath，网页展示与最终获取的网页可能会有很大差别
			//CreatHtmlFile("d:/test.html",html);
			switch (Domain) {
			case "xicidaili.com":
				AnalyseXICI XICI = new AnalyseXICI();
				XICI.AnalyseHTML(html);
				break;
			case "proxy360.cn":
				AnalyseProxy360 Proxy360 = new AnalyseProxy360();
				Proxy360.AnalyseHTML(html);
				break;
			}

			/*
			 * String text = htmlParseData.getText(); String html =
			 * htmlParseData.getHtml();
			 * 
			 * //CreatConfigFile("d:/test.html",html); List<WebURL> links =
			 * htmlParseData.getOutgoingUrls();
			 * 
			 * System.out.println("Text length: " + text.length());
			 * System.out.println("Html length: " + html.length());
			 * System.out.println("Number of outgoing links: " + links.size());
			 */
		}

		// System.out.println("=============");
	}

	public void CreatHtmlFile(String Path, String Text) {
		File f = new File(Path);
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				System.out.println(e);
			}
		}

		byte[] bytes = null;
		try {
			bytes = Text.getBytes("UTF-8");// 一定由string得到bytes才能写入到文件里
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		OutputStream os;
		try {
			os = new FileOutputStream(Path);
			os.write(bytes);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}// 将字节一个个写入文件
	}

	public void CreatPath(String Path) {
		File dir = new File(Path);
		if (dir.exists() && dir.isDirectory()) {
			// 目录存在
			return;
		} else {
			// 不存在
			dir.mkdir();
		}
	}

}
