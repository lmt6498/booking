package com.pn.booking.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pn.booking.client.feign.CloudbedsAPIClient;
import com.pn.booking.service.MewsWebhookService;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pn.booking.service.cloudbeds.CloudbedsBookingService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping()
@RequiredArgsConstructor
public class MewsWebhookController extends BaseController {
    private final MewsWebhookService webhookService;
    private final CloudbedsBookingService cloudbedService;

    private final CloudbedsAPIClient cloudbedsAPIClient;

    @PostMapping("/mews/webhook")
    public void receiveMessage(@RequestBody Map<String, Object> messageAsMap) {
        webhookService.receiveMessage(messageAsMap);
    }

    @PostMapping("/cloudbeds/webhook")
    public void receiveWebhookMessage(@RequestBody Map<String, Object> data) {
        cloudbedService.receiveWebhookMessage(data);
    }

    @DeleteMapping("/cloudbeds/webhook/")
    public void deleteAllWebhook(@RequestParam String token) {
        var ids = cloudbedsAPIClient.getAllWebhooks(token);
        var data = (List<Map<String, String>>) ids.get("data");
        List<String> result = data.stream()
                .map(line -> line.get("id"))
                .collect(Collectors.toList());
        result.forEach(item -> {
           cloudbedsAPIClient.deleteWebhooks(token, item);
        });
    }
}
