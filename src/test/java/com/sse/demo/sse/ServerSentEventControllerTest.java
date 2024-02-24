package com.sse.demo.sse;

import com.sse.demo.domain.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class ServerSentEventControllerTest {

  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private OrderRepository orderRepository;
  @Test
  public void shouldSubscribeToStream(){
    ParameterizedTypeReference<ServerSentEvent<String>> type
      = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

    Flux<ServerSentEvent<String>> eventStream = webTestClient.get()
      .uri("/sse-webflux/stream")
      .accept(MediaType.TEXT_EVENT_STREAM)
      .exchange()
      .expectStatus().isOk()
      .returnResult(type)
      .getResponseBody();

    StepVerifier
      .create(eventStream.map(s -> s.id()))
      .expectSubscription()
      .expectNext("SSE_1", "SSE_2", "SSE_3", "SSE_4", "SSE_5")
      .verifyComplete();
  }

  @Test
  public void shouldSubscribeToChronoSeconds(){
    ParameterizedTypeReference<ServerSentEvent<String>> type
      = new ParameterizedTypeReference<ServerSentEvent<String>>() {};

    Flux<ServerSentEvent<String>> eventStream = webTestClient.get()
      .uri("/sse-webflux/seconds")
      .accept(MediaType.TEXT_EVENT_STREAM)
      .exchange()
      .expectStatus().isOk()
      .returnResult(type)
      .getResponseBody();

    StepVerifier
      .create(eventStream.takeUntil(s -> s.id().equals("4")).map(s -> s.id()))
      .expectSubscription()
      .expectNext("0", "1", "2", "3", "4")
      .verifyComplete();
  }
}
