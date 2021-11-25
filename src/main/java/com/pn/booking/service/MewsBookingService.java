package com.pn.booking.service;

import java.util.List;

import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsFetchingReservationResponse;
import com.pn.booking.model.entity.Booking;

public interface MewsBookingService {

  public List<Booking> synchronizeBooking(MewsFetchingReservationResponse mewsResponse, String message, MewsCredentialResponse credentials);

}
