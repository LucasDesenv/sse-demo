package com.sse.demo.sqs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderMessage {

  @JsonProperty("Message")
  private String message;
}
