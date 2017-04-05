package MyCrawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.FileWriter;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

public class Controller
{
    public static void main(String[] args) throws Exception {
	    	    
    	String crawlStorageFolder = "data/crawl";
        String seedURL = "http://abcnews.go.com/";
        
        int maxPagesToFetch = 200;
        int maxDepthOfCrawling = 16;
        int numberOfCrawlers = 16;
        int maxDownloadedSize = 1024 * 1024 * 10;
        int politenessDelay = 100;
    	
	    CrawlConfig config = new CrawlConfig();
	    
	    /**
	     *  Determine where to save files.
	     */
        config.setCrawlStorageFolder(crawlStorageFolder);

        /**
         * i. Crawl depth – There is no depth for crawling by default. 
         * However, you can limit the crawl depth by specifying this parameter in the CrawlConfig object.
		 * E.g. A is the seed URL, and if A->B->C->D is the link structure. 
		 * Setting a crawl depth of 2, will result in crawling/visiting of 
		 * pages A (depth 0), B (depth 1) and C (depth 2) and will avoid crawling page D.
         */
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        
        /** 
         * ii. Maximum number of pages to crawl –
         * To limit the number of pages that are fetched and stored. By default there is no such limit.
         */
        config.setMaxPagesToFetch(maxPagesToFetch);
        
        /** 
         * iii. Politeness – Crawling puts a huge load on servers that you are trying to crawl. 
         * If you bombard the server with multiple requests in a short duration of time, they will block you. 
         * It is really important to crawl politely, and not disrupt the services provided by the server.
         *  By default, crawler4j waits for at least 200ms between requests, but you might want to increase this duration. 
         *  However you should not make it very large, as your crawl might take forever to finish.
         */
         config.setPolitenessDelay(politenessDelay);
 
        /** iv. User Agent – This is part of the politeness policy as well, 
         * identifying yourself to the server you are crawling.
         */
        // config.setUserAgentString(userAgentString);
        
        /**
         *  Set max download file size
         */
        config.setMaxDownloadSize(maxDownloadedSize);
    	
        config.setIncludeBinaryContentInCrawling(true);

        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
	
        /**
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed(seedURL);
        
        /**
	     * Start the crawl. This is a blocking operation, meaning that your code
	     * will reach the line after this only when crawling is finished.
	     */
	    controller.start(MyCrawler.class, numberOfCrawlers);
        
        CrawlStat crawlStat = new CrawlStat();
        List<Object> crawlersLocalData = controller.getCrawlersLocalData();
        for (Object localData : crawlersLocalData) {
            CrawlStat stat = (CrawlStat) localData;
            crawlStat.attemptUrls.addAll(stat.attemptUrls);
            crawlStat.visitedUrls.addAll(stat.visitedUrls);
            crawlStat.discoveredUrls.addAll(stat.discoveredUrls);
        }

        SaveFiles.txtStatistics(crawlStorageFolder, crawlStat);
        SaveFiles.csvFetch(crawlStorageFolder, crawlStat.attemptUrls);
        SaveFiles.csvVisit(crawlStorageFolder, crawlStat.visitedUrls);
        // SaveFiles.csvURL(crawlStorageFolder, crawlStat.discoveredUrls);
    }
}
