package com.pn.booking.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Author: chautn
 */
public class BadRequestException extends RuntimeException {

  @Getter
  private List<String> messages = new ArrayList<>();
  @Getter
  private Map<String, Object> additionalInformation = new HashMap<>();

  public BadRequestException(String message) {
    super(message);
    this.messages.add(message);
  }

  public BadRequestException(List<String> messages) {
    this.messages.addAll(messages);
  }

  public BadRequestException(String message, String key, Object value) {
    super(message);
    this.messages.add(message);
    this.addAdditionalInformation(key, value);
  }

  public BadRequestException addAdditionalInformation(String key, Object value) {
    this.additionalInformation.put(key, value);
    return this;
  }
}
