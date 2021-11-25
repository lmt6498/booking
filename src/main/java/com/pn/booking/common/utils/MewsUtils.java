package com.pn.booking.common.utils;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.request.mews.webhook.MewsGeneralMessageRequest;
import com.pn.booking.model.dto.request.mews.webhook.MewsIntegrationMessageRequest;
import com.pn.booking.model.dto.response.MewsServiceResponse.MewsServiceDataDiscriminator;
import com.pn.booking.model.entity.BookingItem;

import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MewsUtils {
  
  public static Object convertMapToMessage(Map<String, Object> map) {
    try {
      if (isIntegrationMessage(map)) {
        return new ObjectMapper().convertValue(map, MewsIntegrationMessageRequest.class);
      } else if (isGeneralMessage(map)) {
        return new ObjectMapper().convertValue(map, MewsGeneralMessageRequest.class);
      }
      log.warn("Unknown MEWS's message {}", new ObjectMapper().writeValueAsString(map));
    } catch (Exception e) {
      log.error("Could not convert Mews message", e);
    }
    return null;
  }

  public static boolean isIntegrationMessage(Map<String, Object> requestAsMap) {
    return requestAsMap.containsKey("Action");
  }

  public static boolean isGeneralMessage(Map<String, Object> requestAsMap) {
    return requestAsMap.containsKey("EnterpriseId") && requestAsMap.containsKey("IntegrationId");
  }

  public static String normalizeMewsBookingStatus(MewsReservationRequest.State state) {
    switch (state) {
      case CANCELED:
        return BookingStatus.CANCELLED.name();
      case CONFIRMED:
      return BookingStatus.CONFIRMED.name();
      case ENQUIRED:
        return BookingStatus.OTHERS.name();
      case OPTIONAL:
        return BookingStatus.OTHERS.name();
      case PROCESSED:
        return BookingStatus.CHECKED_OUT.name();
      case REQUESTED:
        return BookingStatus.OTHERS.name();
      case STARTED:
        return BookingStatus.CHECKED_IN.name();
      default:
        return BookingStatus.OTHERS.name();
    }
  }

  public static BookingStatus normalizeCloudbedsBookingStatus(String bookingStatus){
    switch (bookingStatus){
      case "no_show":
        return BookingStatus.CONFIRMED;
      case "confirmed":
        return BookingStatus.TO_BE_CHECKED_IN;
      case "canceled":
        return BookingStatus.CANCELLED;
      case "checked_in":
        return BookingStatus.CHECKED_IN;
      case "checked_out":
        return BookingStatus.CHECKED_OUT;
      default:
        return BookingStatus.OTHERS;
    }
  }

  public static MewsReservationRequest.State denormalizeMewsBookingStatus(BookingStatus state) {
    switch (state) {
      case CHECKED_IN:
        return MewsReservationRequest.State.PROCESSED;
      case CHECKED_OUT:
        return MewsReservationRequest.State.STARTED;
      case CANCELLED:
        return MewsReservationRequest.State.CANCELED;
      case CONFIRMED:
        return MewsReservationRequest.State.CONFIRMED;
      default:
        return null;
    }
  }

  // public static String normalizeMewsBookingItemType(MewsServiceDataDiscriminator discriminator) {
  //   if (discriminator.equals(MewsServiceDataDiscriminator.BOOKABLE)) {

  //   }
  // }

}
