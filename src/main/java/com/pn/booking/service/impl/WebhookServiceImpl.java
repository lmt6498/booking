package com.pn.booking.service.impl;

import java.util.List;

import com.pn.booking.client.feign.ConnectorClient;
import com.pn.booking.model.dto.request.WebhookCallbackRequest;
import com.pn.booking.service.WebhookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WebhookServiceImpl implements WebhookService {

  @Autowired
  private ConnectorClient connectorClient;

  @Override
  public boolean fireWebhook(String event, Long businessId, String businessType) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    WebhookCallbackRequest callbackRequest = new WebhookCallbackRequest();
    callbackRequest.setBusinessId(businessId);
    callbackRequest.setBusinessType(businessType);
    callbackRequest.setEvent(event);

    HttpEntity<WebhookCallbackRequest> request = new HttpEntity<>(callbackRequest, headers);
    List<String> urls = connectorClient.getWebhookUrls(businessId, businessType, event).getBody().getData();
    for (String url : urls) {
      try {
        restTemplate.exchange(url, HttpMethod.POST, request, Void.class);
        log.info("callback url has been notified!");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return true;
  }
  
}
