package com.sse.demo.sse;

import com.sse.demo.domain.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class WordControllerTest {
  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private OrderRepository orderRepository;

  @Test
  void shouldSubscribeToWords() {
    webTestClient.post().uri("/words")
      .bodyValue("Hello world")
      .exchange()
      .expectStatus()
      .isCreated();
    Flux<String> response = webTestClient.get().uri("/words").exchange().returnResult(String.class)
      .getResponseBody();
    StepVerifier.create(response)
      .expectNext("Hello world")
      .thenCancel()
      .verify();
  }

  @Test
  void shouldSubscribeOnlyToTheLatestWords() {
    for (int i = 0; i < 3; i++) {
      webTestClient.post().uri("/words")
        .bodyValue("Hello world_"+i)
        .exchange()
        .expectStatus()
        .isCreated();

    }
    Flux<String> response = webTestClient.get().uri("/words").exchange().returnResult(String.class)
      .getResponseBody();
    StepVerifier.create(response)
      .expectNext("Hello world_2")
      .thenCancel()
      .verify();
  }
}
