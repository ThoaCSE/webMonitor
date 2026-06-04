package model;

import java.net.URL;
import java.util.Scanner;

public class WebsiteMonitor {

    public String downloadContent(String url) throws Exception {
        Scanner scanner = new Scanner(new URL(url).openStream()).useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    public boolean hasChanged(String oldContent, String newContent) {
        return !oldContent.equals(newContent);
    }
}