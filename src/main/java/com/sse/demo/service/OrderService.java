package com.sse.demo.service;

import com.sse.demo.domain.Order;
import com.sse.demo.domain.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  public Flux<Order> findAll() {
    return orderRepository.findAll();
  }

  public Mono<Order> save(Order order) {
    return orderRepository.save(order);
  }
}
