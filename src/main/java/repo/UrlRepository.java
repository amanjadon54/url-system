package repo;

import dao.UrlInfo;
import lombok.Data;

import java.util.concurrent.ConcurrentHashMap;

@Data
public class UrlRepository {

    private ConcurrentHashMap<String, UrlInfo> usedUrls;
    private ConcurrentHashMap<String, UrlInfo> softDeletedUrls;

    public UrlRepository() {
        usedUrls = new ConcurrentHashMap<>();
        softDeletedUrls = new ConcurrentHashMap<>();
    }

}
