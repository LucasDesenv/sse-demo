package com.sse.demo.sse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RestController
@RequestMapping("/words")
@Tag(name = "Words event-simulation")
public class WordsAPI {

  private final Sinks.Many<String> wordsStream = Sinks.many().replay().latest();
  @Operation(summary = "Adds a new word")
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<String> addWord(@RequestBody String word) {
    return Mono.just(word)
      .doOnNext(wordsStream::tryEmitNext);
  }

  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<String> wordsStream() {
    return wordsStream.asFlux();
  }

}
