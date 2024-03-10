package com.sse.demo.rest;

import com.sse.demo.domain.Order;
import com.sse.demo.service.OrderService;
import com.sse.demo.sqs.OrderMessage;
import com.sse.demo.sqs.OrderNotifier;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders")
@AllArgsConstructor
public class OrderController {
  private final OrderService orderService;
  private final OrderNotifier orderNotifier;

  @GetMapping
  public Flux<Order> findAll() {
    return orderService.findAll();
  }

  @PostMapping
  public ResponseEntity<Mono<Order>> save(@RequestBody Order order) {
    Mono<Order> orderSaved = orderService.save(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderSaved);
  }

  @GetMapping("/notifications")
  public Flux<ServerSentEvent<OrderMessage>> notifications() {
    return orderNotifier.getOrdersStream().map(s -> ServerSentEvent.builder(s).id(s.getMessage()).build());
  }


}
