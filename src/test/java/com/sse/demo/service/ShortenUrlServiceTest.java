package com.sse.demo.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ShortenUrlServiceTest {

  private static final String DOMAIN = "http://sse.in";
  private ShortenUrlService shortenUrlService;

  @BeforeEach
  void setUp() {
    shortenUrlService = new ShortenUrlService(DOMAIN);
  }

  @Test
  void shouldShortUrl() {
    String originalUrl = "https://www.google.com";

    String shortUrl = shortenUrlService.shortUrl(originalUrl);

    Assertions.assertThat(shortUrl).contains(DOMAIN);
    Assertions.assertThat(shortUrl.replace(DOMAIN, "")).contains("/");
    Assertions.assertThat(shortUrl.replace(DOMAIN, "").length()).isGreaterThan(10);
  }

  @ParameterizedTest
  @ValueSource(strings = {"https://www.google.com", "https://www.google.com/", "http://www.google.com",
    "www.google.com", "www.google.com/"})
  void shouldSetSameShortUrlForSimilarUrls(String googleUrl) {
    String expectedUrlForGoogle = shortenUrlService.shortUrl("https://www.google.com");

    String actualShortUrl = shortenUrlService.shortUrl(googleUrl);

    Assertions.assertThat(actualShortUrl).isEqualTo(expectedUrlForGoogle);
    Assertions.assertThat(shortenUrlService.getShortUrl()).hasSize(1);
    Assertions.assertThat(shortenUrlService.getLongUrls()).hasSize(1);
  }

  @ParameterizedTest
  @ValueSource(strings = {"https://www.google.com", "https://www.google.com/", "http://www.google.com",
    "www.google.com", "www.google.com/"})
  void shouldGetSameShortUrlForSimilarUrls(String googleUrl) {
    String shortUrl = shortenUrlService.shortUrl(googleUrl);
    String decoded = shortenUrlService.decode(shortUrl);

    Assertions.assertThat(decoded).isEqualTo("www.google.com");
    Assertions.assertThat(shortenUrlService.getShortUrl()).hasSize(1);
    Assertions.assertThat(shortenUrlService.getLongUrls()).hasSize(1);
  }

  @Test
  public void testConcurrency() throws InterruptedException {
    final int numThreads = 1_000;
    final int numIterations = 1_000;
    final CountDownLatch latch = new CountDownLatch(numThreads);

    ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

    for (int i = 0; i < numThreads; i++) {
      executorService.submit(() -> {
        try {
          for (int j = 0; j < numIterations; j++) {
            String originalUrl = "http://example.com/";
            shortenUrlService.shortUrl(originalUrl);
          }
        } finally {
          latch.countDown();
        }
      });
    }

    latch.await(); // Wait for all threads to finish
    executorService.shutdown();

    // Check that the expected number of unique URLs are stored
    Assertions.assertThat(shortenUrlService.getLongUrls()).hasSize(1);
    Assertions.assertThat(shortenUrlService.getShortUrl()).hasSize(1);

  }
}
