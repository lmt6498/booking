package com.pn.booking.client.feign;

import com.pn.booking.model.dto.response.cloudbeds.CloudBedReservationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "CloudbedClient", url = "https://hotels.cloudbeds.com/api/v1.1")
public interface CloudbedsAPIClient {

    @GetMapping("/getReservations")
    Map<String, Object> getReservations(@RequestHeader("Authorization") String token, @RequestParam(defaultValue = "1", required = false) String includeGuestsDetails);

    @GetMapping("/getReservation")
    Map<String, Object> getReservationById(@RequestHeader("Authorization") String token, @RequestParam("reservationID") String reservationId);

    @GetMapping("/getWebhooks")
    Map<String, Object> getAllWebhooks(@RequestHeader("Authorization") String token);

    @DeleteMapping("/deleteWebhook")
   void deleteWebhooks(@RequestHeader("Authorization") String token, @RequestParam("subscriptionID") String subscriptionID);

}
