package MyCrawler;

import com.google.common.io.Files;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {
    // private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$");
	// private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|mp3|zip|gz))$");
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|" + "|mp3|mp3|zip|gz))$");
	
    CrawlStat crawlState;

    public MyCrawler() {
        crawlState = new CrawlStat();
    }

    private static File storageFolder;

    public static void configure(String storageFolderName) {
        storageFolder = new File(storageFolderName);
        if (!storageFolder.exists()) {
            storageFolder.mkdirs();
        }
    }

    @Override
    public boolean shouldVisit(Page page, WebURL url) {
        String href = url.getURL().toLowerCase();
        String type = "N_OK";
       
        // if (href.contains("http://abcnews.go.com") || href.contains("https://abcnews.go.com")) {
        if(href.length() >= 22) {
	        if (href.substring(0,22).equals("http://abcnews.go.com/")
	        		|| href.substring(0,22).equals("https://abcnews.go.com")) {
	        	type = "OK";
	        }
        }

        crawlState.discoveredUrls.add(new UrlInfo(href, type));
        return !FILTERS.matcher(href).matches() && type.equals("OK");
    }

    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        String contentType = "wrong format";
        if(page.getContentType() != null) {
        	contentType = page.getContentType().split(";")[0];
        }
        ArrayList<String> outgoingUrls = new ArrayList<String>();

        UrlInfo urlInfo;
        if (contentType.equals("text/html")) { // html
            if (page.getParseData() instanceof HtmlParseData) {
                HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
                Set<WebURL> links = htmlParseData.getOutgoingUrls();
                for (WebURL link : links) {
                    outgoingUrls.add(link.getURL());
                }
                urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "text/html", ".html");
                crawlState.visitedUrls.add(urlInfo);
            } else {
                urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "text/html", ".html");
                crawlState.visitedUrls.add(urlInfo);
            }
        } else if (contentType.equals("application/msword")) { // doc
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "application/msword", ".doc");
            crawlState.visitedUrls.add(urlInfo);
        } else if (contentType.equals("application/pdf")) { // pdf
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "application/pdf", ".pdf");
            crawlState.visitedUrls.add(urlInfo);
        } else if (contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ".docx");
            crawlState.visitedUrls.add(urlInfo);
        } else if (contentType.equals("image/gif")) { // gif
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "image/git", ".gif");
            crawlState.visitedUrls.add(urlInfo);
        } else if (contentType.equals("image/jpeg")) { // jpeg
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "image/jpeg", ".jpeg");
            crawlState.visitedUrls.add(urlInfo);
        } else if (contentType.equals("image/png")) { // png
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "image/png", ".png");
            crawlState.visitedUrls.add(urlInfo);
        } else {
            urlInfo = new UrlInfo(url, page.getContentData().length, outgoingUrls, "unknown", "");
            crawlState.visitedUrls.add(urlInfo);
        }
    }

    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        crawlState.attemptUrls.add(new UrlInfo(webUrl.getURL(), statusCode));
    }

    @Override
    public Object getMyLocalData() {
        return crawlState;
    }
}