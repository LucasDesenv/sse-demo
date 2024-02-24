package com.sse.demo.rest;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sse-webflux")
public class ServerSentEventController {

  private static List<String> WORDS = Arrays.asList("SSE_1", "SSE_2", "SSE_3", "SSE_4", "SSE_5");

  @GetMapping("/stream")
  public Flux<ServerSentEvent<String>> wordsStreamEvents() {
    return Flux.fromStream(WORDS.stream()).map(
      word -> ServerSentEvent.<String>builder().id(String.valueOf(word)).event("periodic-event")
        .data("SSE - " + LocalDateTime.now())
        .build());
  }

  @GetMapping("/seconds")
  public Flux<ServerSentEvent<String>> everySecondEvents() {
    return Flux.interval(Duration.of(1, ChronoUnit.SECONDS))
      .map(sequence -> ServerSentEvent.<String>builder().id(String.valueOf(sequence)).event("periodic-event")
        .data("SSE - " + LocalDateTime.now())
        .build());
  }
}
