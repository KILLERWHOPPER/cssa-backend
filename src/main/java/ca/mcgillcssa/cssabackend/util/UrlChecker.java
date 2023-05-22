package ca.mcgillcssa.cssabackend.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class UrlChecker {

    private boolean valid;
    private String url;

    public UrlChecker(boolean b, String imgUrl) {
        this.valid = b;
        this.url = imgUrl;
    }

    /**
     * Add https:// if not present to the url, and check if the url is valid
     * 
     * @param imgUrl the url
     * @return UrlChecker object containing the result of the check and the modified
     *         url
     * @throws IOException if the url is invalid
     * @author Zihan Zhang
     */
    public static UrlChecker isValidUrl(String imgUrl) throws IOException {
        if (!imgUrl.startsWith("http://") && !imgUrl.startsWith("https://")) {
            imgUrl = "https://" + imgUrl;
        }
        URL url = new URL(imgUrl);
        HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("HEAD");
        return new UrlChecker(huc.getResponseCode() == 200, imgUrl);
    }

    public boolean isValid() {
        return this.valid;
    }

    public String getUrl() {
        return this.url;
    }
}
