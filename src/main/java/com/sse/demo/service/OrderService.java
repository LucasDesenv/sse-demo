package com.sse.demo.service;

import com.sse.demo.domain.Order;
import com.sse.demo.domain.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@AllArgsConstructor
public class OrderService {
  private final OrderRepository orderRepository;
  public List<Order> findAll() {
    Iterable<Order> all = orderRepository.findAll();
    List<Order> orders = new ArrayList<>();
    all.forEach(orders::add);
    return orders;
  }

  public Order save(Order order) {
    return orderRepository.save(order);
  }
}
