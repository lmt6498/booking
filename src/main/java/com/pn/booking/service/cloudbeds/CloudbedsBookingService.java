package com.pn.booking.service.cloudbeds;

import com.pn.booking.model.entity.Booking;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CloudbedsBookingService {
    void receiveWebhookMessage(Map<String, Object> data);
    List<Booking> synchronizeBooking(Long venueId, boolean isSyncAll);

}
