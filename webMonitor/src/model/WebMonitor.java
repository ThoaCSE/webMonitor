package model;

import strategy.WebComparison;
import strategy.HtmlComparison;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class WebMonitor {
    // Luôn giữ tham chiếu lỏng qua Interface thay vì lớp cụ thể
    private WebComparison comparisonStrategy;

    public WebMonitor() {
        this.comparisonStrategy = new HtmlComparison();
    }

    public void setComparisonStrategy(WebComparison comparisonStrategy) {
        this.comparisonStrategy = comparisonStrategy;
    }

    public String downloadContent(String url) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/126.0.0.0 Safari/537.36");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        connection.setRequestProperty("Referer", "https://www.google.com/");
        connection.setRequestProperty("Connection", "keep-alive");
        connection.setRequestProperty("Upgrade-Insecure-Requests", "1");
        connection.setRequestProperty("Sec-Fetch-Dest", "document");
        connection.setRequestProperty("Sec-Fetch-Mode", "navigate");
        connection.setRequestProperty("Sec-Fetch-Site", "none");
        connection.setRequestProperty("Sec-Fetch-User", "?1");

        int responseCode = connection.getResponseCode();
        if (responseCode >= 400) {
            throw new Exception("Server returned HTTP response code: " + responseCode + " for URL: " + url);
        }

        String encoding = connection.getContentEncoding();
        try (InputStream rawInput = connection.getInputStream();
             InputStream input = "gzip".equalsIgnoreCase(encoding)
                     ? new GZIPInputStream(rawInput)
                     : "deflate".equalsIgnoreCase(encoding)
                         ? new InflaterInputStream(rawInput)
                         : rawInput;
             Scanner scanner = new Scanner(input).useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        } finally {
            connection.disconnect();
        }
    }

    public boolean hasChanged(String oldContent, String newContent) {
        return comparisonStrategy.hasChanged(oldContent, newContent);
    }
}