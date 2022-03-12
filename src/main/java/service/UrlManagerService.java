package service;

import java.util.List;

public interface UrlManagerService {

    String shortendUrl(String url, long ttl);

    String getUrl(String shortenUrl);

    void softInvalidateUrls(List<String> shortenedUrls);

    void reshuffleUrls(List<String> shortenedUrls);

}
