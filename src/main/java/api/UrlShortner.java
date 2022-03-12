package api;

public interface UrlShortner {

    String get(String shortenedUrl);

    String put(String originalUr, String userId, long ttl);

    String put(String originalUrl, long ttl);

    void deactivate(String userId);

    void reactivate(String userid);

}
