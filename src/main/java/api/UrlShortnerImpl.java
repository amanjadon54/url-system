package api;

import lombok.AllArgsConstructor;
import model.User;
import service.UrlManagerService;
import service.UserService;

import java.util.List;

import static constant.shortnerConstant.DEFAULT_FORVER_TTL;

@AllArgsConstructor
public class UrlShortnerImpl implements UrlShortner {

    private UrlManagerService urlManagerService;
    private UserService userService;

    @Override
    public String get(String shortenedUrl) {
        return urlManagerService.getUrl(shortenedUrl);
    }

    @Override
    public String put(String originalUrl, String userId, long ttl) {
        User user = userService.get(userId);
        String shortenedUrl = urlManagerService.shortendUrl(originalUrl, ttl == 0 ? DEFAULT_FORVER_TTL : ttl);
        user.getUrls().add(shortenedUrl);
        return shortenedUrl;
    }

    @Override
    public String put(String originalUrl, long ttl) {
        return urlManagerService.shortendUrl(originalUrl, ttl);
    }

    @Override
    public void deactivate(String userId) {
        User user = userService.get(userId);
        user.setActive(false);
        List<String> urls = user.getUrls();
        urlManagerService.softInvalidateUrls(urls);
    }

    @Override
    public void reactivate(String userId) {
        User user = userService.get(userId);
        user.setActive(true);
        List<String> urls = user.getUrls();
        urlManagerService.reshuffleUrls(urls);
    }
}
