package model;

import strategy.WebComparison;
import strategy.HtmlComparison;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WebMonitor {
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

        int responseCode = connection.getResponseCode();
        if (responseCode >= 400) {
            throw new Exception("Server returned HTTP response code: " + responseCode + " for URL: " + url);
        }

        try (InputStream input = connection.getInputStream();
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