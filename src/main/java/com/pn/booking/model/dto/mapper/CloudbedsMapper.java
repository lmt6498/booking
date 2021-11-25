package com.pn.booking.model.dto.mapper;

import com.pn.booking.model.dto.response.cloudbeds.CloudBedReservationResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingItem;

import java.util.ArrayList;
import java.util.List;

public class CloudbedsMapper {

    public static List<Booking> fromCloudbedsReservations(List<CloudBedReservationResponse> responses) {
        List<Booking> bookings = new ArrayList<>();

        for (CloudBedReservationResponse item : responses) {
            BookingItem bookingItem;
        }

        return bookings;
    }

}
