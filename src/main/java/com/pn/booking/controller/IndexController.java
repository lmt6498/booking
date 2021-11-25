package com.pn.booking.controller;

import java.util.Map;

import com.pn.booking.service.MewsWebhookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  @GetMapping
  public String index() {
    return "ms-bookings";
  }

  @Autowired
  private MewsWebhookService webhookService;

  @PostMapping
  public void receiveMessage(@RequestBody Map<String, Object> messageAsMap) {
    webhookService.receiveMessage(messageAsMap);
  }

}
