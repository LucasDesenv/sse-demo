package com.sse.demo.sqs;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class OrderNotifier {
  private final Sinks.Many<OrderMessage> ordersStream;

  public OrderNotifier(){
    Sinks.Many<OrderMessage> onlyLatestEvents = Sinks.many().replay().latest();
    ordersStream = onlyLatestEvents;
  }

  void notifyNewOrder(OrderMessage message){
    ordersStream.tryEmitNext(message);
  }

  public Flux<OrderMessage> getOrdersStream() {
    return ordersStream.asFlux();
  }
}
