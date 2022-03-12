package dao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UrlInfo {

    private String originalUrl;
    private long ttl;
    private long creationTimeInMs;
}
