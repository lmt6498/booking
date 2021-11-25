package com.pn.booking.model.dto;

import org.springframework.http.ResponseEntity;

public class DtoUtils {

  public static <T> RestResult<T> toRestResult(ResponseEntity<RestResult<T>> response) {
    return response.getBody();
  }

  public static <T> T toData(ResponseEntity<RestResult<T>> response) {
    RestResult<T> result = toRestResult(response);
    return result == null ? null : result.getData();
  }
}
