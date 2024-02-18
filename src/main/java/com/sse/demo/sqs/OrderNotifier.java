package com.sse.demo.sqs;

import com.sse.demo.domain.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class OrderNotifier {
  private final Sinks.Many<Order> ordersStream;

  public OrderNotifier(){
    Sinks.Many<Order> onlyLatestEvents = Sinks.many().replay().latest();
    ordersStream = onlyLatestEvents;
  }

  void notifyNewOrder(Order order){
    ordersStream.tryEmitNext(order);
  }

  public Flux<Order> getOrdersStream() {
    return ordersStream.asFlux();
  }
}
