package MyCrawler;

import java.util.ArrayList;

public class UrlInfo {
    public int statusCode;
    public String url;
    public int size;
    public String type;
    public ArrayList<String> outgoingUrls;
    public String extension;

    public UrlInfo(String url, int statusCode) {
        this.url = url;
        this.statusCode = statusCode;
    }

    public UrlInfo(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public UrlInfo(String url, int size, ArrayList<String> outgoingUrls, String type, String extenstion) {
        this.url = url;
        this.size = size;
        this.outgoingUrls = outgoingUrls;
        this.type = type;
        this.extension = extenstion;
    }
}
