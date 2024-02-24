package com.sse.demo.domain.url;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Base64;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Url {
  // Regex pattern to exclude www, http://, and https://, and remove the ending slash
  private static final String SANITIZER_REGEX = "(?i)(www\\\\.|http://|https://)|/$";
  private Integer id;
  private String originalUrl;
  private String shortUrl;

  public Url(String originalUrl) {
    this.originalUrl = sanitizeURL(originalUrl);
    this.shortUrl = Base64.getUrlEncoder().encodeToString(this.originalUrl.getBytes());
    this.id = shortUrl.hashCode();
  }

  public static String sanitizeURL(String url) {
    return url.replaceAll(SANITIZER_REGEX, "");
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public String getShortUrl(String domain) {
    return domain.concat("/").concat(shortUrl);
  }
}
