package com.pn.booking.model.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus.Series;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestTemplateUtils {

  @Getter
  @AllArgsConstructor
  public static class RestRequest<V> {

    private String url;
    private V request;
  }

  public static <T,V> T post(RestRequest<V> restRequest, Class<T> responseClazz) {
    return fetch(restRequest, responseClazz, HttpMethod.POST);
  }

  private static <T, V> T fetch(RestRequest<V> restRequest, Class<T> responseClazz, HttpMethod method) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    HttpEntity<V> httpRequest = new HttpEntity<>(restRequest.getRequest());
    ResponseEntity<T> responseEntity = restTemplate.exchange(restRequest.getUrl(), method, httpRequest, responseClazz);
    return responseEntity.getBody();
  }

}
