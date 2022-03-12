package service.impl;

import dao.UrlInfo;
import lombok.AllArgsConstructor;
import repo.UrlRepository;
import service.UrlManagerService;
import strategy.UniqueKeyStrategy;

import java.util.List;

import static constant.shortnerConstant.DEFAULT_FORVER_TTL;
import static constant.shortnerConstant.INVALID_URL;

@AllArgsConstructor
public class UrlManagerServiceImpl implements UrlManagerService {

    private UrlRepository urlRepository;
    private UniqueKeyStrategy uniqueKeyStrategy;

    public String shortendUrl(String originalUrl, long ttl) {

        while (true) {
            String genratedUrl = uniqueKeyStrategy.generate();
            if (!urlRepository.getUsedUrls().contains(genratedUrl)) {
                UrlInfo urlInfo = new UrlInfo(originalUrl, ttl, System.currentTimeMillis());
                urlRepository.getUsedUrls().put(genratedUrl, urlInfo);
                return genratedUrl;
            }
        }

    }

    public String getUrl(String shortenUrl) {
        UrlInfo urlInfo = urlRepository.getUsedUrls().get(shortenUrl);
        if (urlInfo == null) {
            throw new RuntimeException(INVALID_URL);
        }

        if (urlInfo.getTtl() != DEFAULT_FORVER_TTL && (System.currentTimeMillis() - urlInfo.getCreationTimeInMs()) >= urlInfo.getTtl()) {
            invalidateUrl(shortenUrl);
            throw new RuntimeException(INVALID_URL);
        }

        return urlInfo.getOriginalUrl();

    }

    private void invalidateUrl(String shortenUrl) {
        urlRepository.getUsedUrls().remove(shortenUrl);
    }

    @Override
    public void softInvalidateUrls(List<String> shortenedUrls) {
        for (String shortUrl : shortenedUrls) {
            if (urlRepository.getUsedUrls().containsKey(shortUrl)) {
                UrlInfo urlInfo = urlRepository.getUsedUrls().remove(shortUrl);
                urlRepository.getSoftDeletedUrls().put(shortUrl, urlInfo);
            }
        }
    }

    @Override
    public void reshuffleUrls(List<String> shortenedUrls) {
        for (String shortUrl : shortenedUrls) {
            if (urlRepository.getSoftDeletedUrls().containsKey(shortUrl)) {
                UrlInfo urlInfo = urlRepository.getSoftDeletedUrls().remove(shortUrl);
                urlRepository.getUsedUrls().putIfAbsent(shortUrl, urlInfo);
            }
        }
    }


}
