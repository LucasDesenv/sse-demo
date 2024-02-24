package com.sse.demo.service;

import com.sse.demo.domain.url.Url;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShortenUrlService {

  private final Map<String, Url> shortUrl = new ConcurrentHashMap<>();
  private final Map<String, Url> longUrls = new ConcurrentHashMap<>();
  private final String domain;

  public ShortenUrlService(@Value("${sse.demo.domain:http://sse.in}") String domain) {
    this.domain = domain;
  }

  public String shortUrl(String originalUrl) {
    Url existingLongUrl = longUrls.get(Url.sanitizeURL(originalUrl));

    if (existingLongUrl != null){
      return existingLongUrl.getShortUrl(domain);
    }

    Url url = new Url(originalUrl);
    shortUrl.putIfAbsent(url.getShortUrl(domain), url);
    longUrls.putIfAbsent(url.getOriginalUrl(), url);
    return url.getShortUrl(domain);
  }

  public String decode(String url) {
    return shortUrl.getOrDefault(url, new Url()).getOriginalUrl();
  }

  public Map<String, Url> getLongUrls() {
    return longUrls;
  }

  public Map<String, Url> getShortUrl() {
    return shortUrl;
  }
}
