package com.sse.demo.sqs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class OrderListener {
  private final OrderNotifier orderNotifier;
  private final ObjectMapper objectMapper;
  public void receiveMessage(String message){

    OrderMessage orderMessage = null;
    try {
      orderMessage = objectMapper.readValue(message, OrderMessage.class);
      log.info("Message received: {}", orderMessage);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException(e);
    }

    orderNotifier.notifyNewOrder(orderMessage);
  }

  public void receiveFailedMessage(String message){
    log.error("Dead Letter message received: {}", message);
  }
}
