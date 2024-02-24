package com.sse.demo.sqs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OrderMessage {

  @JsonProperty("Message")
  private String message;
}
