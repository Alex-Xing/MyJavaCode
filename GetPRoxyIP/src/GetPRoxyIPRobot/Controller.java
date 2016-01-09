package GetPRoxyIPRobot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller implements Runnable {
	public void run() {
		/*
		 * if (args.length != 2) { System.out.println("Needed parameters: ");
		 * System.out .println(
		 * "\t rootFolder (it will contain intermediate crawl data)");
		 * System.out .println(
		 * "\t numberOfCralwers (number of concurrent threads)"); return; }
		 */

		/*
		 * crawlStorageFolder is a folder where intermediate crawl data is
		 * stored.
		 */
		// String crawlStorageFolder = args[0];
		String crawlStorageFolder = "/data/crawl/root";

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		// int numberOfCrawlers = Integer.parseInt(args[1]);
		int numberOfCrawlers = 7;

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		config.setPolitenessDelay(1000);

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		config.setMaxDepthOfCrawling(4);

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		config.setMaxPagesToFetch(100000);

		/*
		 * Do you need to set a proxy? If so, you can use:
		 * config.setProxyHost("proxyserver.example.com");
		 * config.setProxyPort(8080);
		 * 
		 * If your proxy also needs authentication:
		 * config.setProxyUsername(username); config.getProxyPassword(password);
		 * Comment by tharindu...@gmail.com, Sep 15, 2013:
		 * 
		 * Isn't it proxy.setProxyPassword(password) ?
		 */

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		config.setResumableCrawling(true);

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		CrawlController controller = null;
		try {
			controller = new CrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		// 设置要爬的网址，可以多个；
		String[] crawlerDomains = new String[] { "http://www.kuaidaili.com/"};//,"http://www.proxy360.cn/", "http://www.xicidaili.com/" };
		controller.setCustomData(crawlerDomains);
		for (String MycrawlDomain : crawlerDomains) {
			controller.addSeed(MycrawlDomain);// 把全部要爬的网址，设置为种子地址
		}

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(BasicCrawler.class, numberOfCrawlers);
	}

	public static void main(String[] args) {
		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		Controller test = new Controller();
		//test.run();//system.out.println("test");
		scheduledThreadPool.scheduleAtFixedRate(test, 0, 1, TimeUnit.DAYS);

	}

}
