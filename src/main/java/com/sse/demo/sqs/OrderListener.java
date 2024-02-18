package com.sse.demo.sqs;

import com.sse.demo.domain.Order;
import lombok.AllArgsConstructor;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrderListener {
  private final OrderNotifier orderNotifier;
  @SqsListener("${cloud.aws.sqs.orders-queue}")
  public void receiveMessage(Order message){
    orderNotifier.notifyNewOrder(message);
  }
}
