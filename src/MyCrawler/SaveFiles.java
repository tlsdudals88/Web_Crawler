package MyCrawler;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SaveFiles {
	 public static void csvFetch(String crawlStorageFolder, ArrayList<UrlInfo> attemptUrls) throws Exception {
	        String fileName = crawlStorageFolder + "/fetch_NewsSite.csv";
	        FileWriter writer = new FileWriter(fileName);
	        // writer.append("URL,Status Code\n");
	        for (UrlInfo info : attemptUrls) {
	            writer.append(info.url + "," + info.statusCode + "\n");
	        }
	        writer.flush();
	        writer.close();
	    }

	    public static void csvVisit(String crawlStorageFolder, ArrayList<UrlInfo> visitedUrls) throws Exception {
	        String fileName = crawlStorageFolder + "/visit.csv";
	        FileWriter writer = new FileWriter(fileName);
	        // writer.append("URL,Size,# OutLinks,Content-type\n");
	        for (UrlInfo info : visitedUrls) {
	            if (info.type != "unknown") {
	                writer.append(info.url + "," + info.size + "," + info.outgoingUrls.size() + "," + info.type + "\n");
	            }
	        }
	        writer.flush();
	        writer.close();
	    }

	    public static void csvURL(String crawlStorageFolder, ArrayList<UrlInfo> discoveredUrls) throws Exception {
	        String fileName = crawlStorageFolder + "/urls.csv";
	        FileWriter writer = new FileWriter(fileName);
	        // writer.append("URL,Type\n");
	        for (UrlInfo info : discoveredUrls) {
	            writer.append(info.url + "," + info.type + "\n");
	        }
	        writer.flush();
	        writer.close();
	    }

	    public static void txtStatistics(String crawlStorageFolder, CrawlStat crawlStat) throws Exception {
	        String fileName = crawlStorageFolder + "/CrawlReport_NewsSite.txt";
	        FileWriter writer = new FileWriter(fileName);
	        
	        // Personal Information
	        writer.append("Name: Youngmin Shin \n");
	        writer.append("USC ID: 2794 6231 67 \n");
	        writer.append("News site crawled: abcnews.go.com \n");
	        writer.append("\n");

	        // Fetch Statistics
	        writer.append("Fetch Statistics \n");
	        writer.append("===================== \n");
	        writer.append("# fetches attempted: " + crawlStat.attemptUrls.size() + "\n");
	        // writer.append("# fetched succeeded: " + crawlStat.visitedUrls.size() + "\n");

	        int failedUrlsCount = 0;
	        int abortedUrlsCount = 0;
	        int succeededUrlsCount = 0;
	        for (UrlInfo info : crawlStat.attemptUrls) {
	        	if (info.statusCode == 200) {
	        		succeededUrlsCount++;
	        	}
	        	else {
	        		failedUrlsCount++;
	        	}
	        	/*
	        	else if (info.statusCode >= 300 && info.statusCode < 400) {
	                abortedUrlsCount++;
	            } 
	        	else {
	                failedUrlsCount++;
	            }
	            */
	        }
	        
	        // Time out!!
	        abortedUrlsCount = succeededUrlsCount - crawlStat.visitedUrls.size();
	        
	        failedUrlsCount = failedUrlsCount - abortedUrlsCount;

	        writer.append("# fetched succeeded: " + succeededUrlsCount + "\n");
	        writer.append("# fetched aborted: " + abortedUrlsCount + "\n");
	        writer.append("# fetched failed: " + failedUrlsCount + "\n");
	        writer.append("# fetched visited: " + crawlStat.visitedUrls.size() + "\n");
	        writer.append("\n");

	        // Outgoing URLS
	        HashSet<String> hashSet = new HashSet<String>();
	        int uniqueUrls = 0;
	        int inUrls = 0;
	        // int schoolUrls = 0;
	        // int uscUrls = 0;
	        int outUrls = 0;
	        writer.append("Outgoing URLs \n");
	        writer.append("===================== \n");
	        writer.append("Total URLs extracted: " + crawlStat.discoveredUrls.size() + "\n");
	        for (UrlInfo info : crawlStat.discoveredUrls) {
	            if (!hashSet.contains(info.url)) {
	                hashSet.add(info.url);
	                uniqueUrls++;
	                if (info.type.equals("OK")) {
	                	inUrls++;
	                // } else if (info.type.equals("USC")) {
	                //    uscUrls++;
	                } else {
	                    outUrls++;
	                }
	            }
	        }
	        writer.append("# unique URLs extracted: " + uniqueUrls + "\n");
	        writer.append("# unique URLs within News Site: " + inUrls + "\n");
	        writer.append("# unique URLs outside News Site: " + outUrls + "\n");
	        // writer.append("# unique USC outside USC: " + outUrls + "\n");
	        writer.append("\n");

	        // Status Code
	        writer.append("Status Codes\n=====================\n");
	        HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
	        for (UrlInfo info : crawlStat.attemptUrls) {
	            if (hashMap.containsKey(info.statusCode)) {
	                hashMap.put(info.statusCode, hashMap.get(info.statusCode) + 1);
	            } else {
	                hashMap.put(info.statusCode, 1);
	            }
	        }
	        HashMap<Integer, String> statusCodeMapping = new HashMap<Integer, String>();
	        statusCodeMapping.put(200, "OK");
	        statusCodeMapping.put(301, "Moved Permanently");
	        statusCodeMapping.put(302, "Found");
	        statusCodeMapping.put(401, "Unauthorized");
	        statusCodeMapping.put(403, "Forbidden");
	        statusCodeMapping.put(404, "Not Found");
	        statusCodeMapping.put(405, "Method Not Allowed");
	        statusCodeMapping.put(500, "Internal Server Error");

	        for (Integer key : hashMap.keySet()) {
	            writer.append("" + key + " " + statusCodeMapping.get(key) + ": " + hashMap.get(key) + "\n");
	        }
	        writer.append("\n");

	        // File Size
	        writer.append("File Size\n=====================\n");
	        int oneK = 0;
	        int tenK = 0;
	        int hundredK = 0;
	        int oneM = 0;
	        int other = 0;
	        for (UrlInfo info : crawlStat.visitedUrls) {
	            if (info.size < 1024) {
	                oneK++;
	            } else if (info.size < 10240) {
	                tenK++;
	            } else if (info.size < 102400) {
	                hundredK++;
	            } else if (info.size < 1024 * 1024) {
	                oneM++;
	            } else {
	                other++;
	            }
	        }
	        writer.append("< 1KB: " + oneK + "\n");
	        writer.append("1KB ~ <10KB: " + tenK + "\n");
	        writer.append("10KB ~ <100KB: " + hundredK + "\n");
	        writer.append("100KB ~ <1MB: " + oneM + "\n");
	        writer.append(">= 1MB: " + other + "\n");
	        writer.append("\n");

	        // Content Types
	        HashMap<String, Integer> hashMap1 = new HashMap<String, Integer>();
	        writer.append("Content Types \n");
	        writer.append("===================== \n");
	        for (UrlInfo info : crawlStat.visitedUrls) {
	            if (info.type.equals("unknown")) {
	                continue;
	            }
	            if (hashMap1.containsKey(info.type)) {
	                hashMap1.put(info.type, hashMap1.get(info.type) + 1);
	            } else {
	                hashMap1.put(info.type, 1);
	            }
	        }
	        for (String key : hashMap1.keySet()) {
	            writer.append("" + key + ": " + hashMap1.get(key) + "\n");
	        }
	        writer.append("\n");

	        writer.flush();
	        writer.close();
	    }
}
