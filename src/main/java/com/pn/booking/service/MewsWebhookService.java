package com.pn.booking.service;

import com.pn.booking.model.dto.request.mews.MewsSyncReservationRequest;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.entity.Booking;
import java.util.List;
import java.util.Map;

public interface MewsWebhookService {

  void receiveMessage(Map<String, Object> messageAsMap);

  List<Booking> synchronizeBooking(MewsSyncReservationRequest request, MewsCredentialResponse credentials);
}
