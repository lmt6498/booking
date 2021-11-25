package com.pn.booking.model.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

/**
 * Author: chautn
 */
public class RestResult<T> {

  public static final String STATUS_SUCCESS = "success";
  public static final String STATUS_ERROR = "error";

  @Getter
  private String status;
  @Getter
  private List<String> messages;
  @Getter
  private T data;
  @Getter
  private Map<String, Object> metadata;

  public RestResult() {
    this.status = STATUS_SUCCESS;
  }

  public RestResult<T> data(T data) {
    this.data = data;
    return this;
  }

  public RestResult<T> metadata(Map<String, Object> metadata) {
    if (this.metadata == null) {
      this.metadata = new HashMap<>();
    }
    if (metadata != null && metadata.size() > 0) {
      this.metadata.putAll(metadata);
    }
    return this;
  }

  public RestResult<T> metadata(String key, Object value) {
    if (metadata == null) {
      this.metadata = new HashMap<>();
    }
    if (key != null && value != null) {
      this.metadata.put(key, value);
    }
    return this;
  }

  public RestResult<T> message(String message) {
    if (messages == null) {
      messages = new ArrayList<>();
    }
    if (message != null && !message.isEmpty()) {
      this.messages.add(message);
    }
    return this;
  }

  public RestResult<T> messages(List<String> messages) {
    if (this.messages == null) {
      this.messages = new ArrayList<>();
    }
    if (messages != null && messages.size() > 0) {
      this.messages.addAll(messages);
    }
    return this;
  }

  public RestResult<T> status(String status) {
    this.status = status;
    return this;
  }
}
