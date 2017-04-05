package MyCrawler;

import java.util.ArrayList;

public class CrawlStat {
    ArrayList<UrlInfo> attemptUrls;
    ArrayList<UrlInfo> visitedUrls;
    ArrayList<UrlInfo> discoveredUrls;

    public CrawlStat() {
        attemptUrls = new ArrayList<UrlInfo>();
        visitedUrls = new ArrayList<UrlInfo>();
        discoveredUrls = new ArrayList<UrlInfo>();
    }
}
