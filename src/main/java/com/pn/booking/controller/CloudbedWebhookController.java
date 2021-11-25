package com.pn.booking.controller;

import com.pn.booking.service.cloudbeds.CloudbedsBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings/cloudbeds/")
public class CloudbedWebhookController extends BaseController {

    private final CloudbedsBookingService service;

    @GetMapping("/reservations")
    public void getReservations(@RequestParam Long venueID, @RequestParam(required = false, defaultValue = "false") boolean isSyncAll) {
        service.synchronizeBooking(venueID, isSyncAll);
    }

    @GetMapping("reservation/{reservationId}/{propertyId}")
    public void reservationById(@PathVariable String reservationId, @PathVariable() String propertyId) {
        // service.getReservationById(reservationId, propertyId);
    }
}
