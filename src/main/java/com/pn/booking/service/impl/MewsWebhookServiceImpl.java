package com.pn.booking.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.pn.booking.client.feign.ConnectorClient;
import com.pn.booking.common.utils.MewsUtils;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.request.mews.MewsReservationSearchRequest;
import com.pn.booking.model.dto.request.mews.MewsSyncReservationRequest;
import com.pn.booking.model.dto.request.mews.webhook.MewsGeneralMessageRequest;
import com.pn.booking.model.dto.request.mews.webhook.MewsIntegrationMessageRequest;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsCustomerResponse;
import com.pn.booking.model.dto.response.MewsFetchingReservationResponse;
import com.pn.booking.model.dto.response.MewsNotesResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.utils.ObjectMapperUtils;
import com.pn.booking.model.utils.RestTemplateUtils;
import com.pn.booking.model.utils.RestTemplateUtils.RestRequest;
import com.pn.booking.service.MewsBookingCustomerService;
import com.pn.booking.service.MewsBookingItemService;
import com.pn.booking.service.MewsBookingNoteService;
import com.pn.booking.service.MewsBookingService;
import com.pn.booking.service.MewsCompanyService;
import com.pn.booking.service.MewsWebhookService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MewsWebhookServiceImpl implements MewsWebhookService {

  private static final Logger logger = LoggerFactory.getLogger(MewsWebhookServiceImpl.class);

  @Autowired
  private ConnectorClient connectorClient;

  @Autowired
  private MewsBookingCustomerService mewsBookingCustomerService;

  @Autowired
  private MewsBookingItemService mewsBookingItemService;

  @Autowired
  private MewsBookingNoteService mewsBookingNoteService;

  @Autowired
  private MewsBookingService mewsBookingService;

  @Value("${mews.all-reservation-url}")
  private String mewsAllReservationUrl;

  @Value("${mews.client-token}")
  private String clientToken;

  @Override
  public void receiveMessage(Map<String, Object> messageAsMap) {
    var message = MewsUtils.convertMapToMessage(messageAsMap);

    Gson gson = new Gson();
    logger.error("message received from mews:");
    logger.error(gson.toJson(message));
    
    if (message != null) {
      if (message instanceof MewsIntegrationMessageRequest) {
        processIntegrationMessage((MewsIntegrationMessageRequest) message);
      } else if (message instanceof MewsGeneralMessageRequest) {
        processGeneralMessage((MewsGeneralMessageRequest) message);
      }
    }
  }

  private void processIntegrationMessage(MewsIntegrationMessageRequest request) {
    connectorClient.syncMewsIntegrationMessage(request);
  }

  private void processGeneralMessage(MewsGeneralMessageRequest request) {
    if (CollectionUtils.isEmpty(request.getEntities().getServiceOrders())) {
      log.error("There are no bookings to update");
      return;
    }

    MewsCredentialResponse credentials = connectorClient.getMewsCustomer(request.getEnterpriseId(), null).getBody().getData();
    if (credentials != null) {
      MewsSyncReservationRequest syncRequest = MewsSyncReservationRequest.builder()
          .enterpriseId(request.getEnterpriseId())
          .reservationIds(request.getEntities().getServiceOrders().stream().map(MewsReservationRequest::getId).collect(Collectors.toList()))
          .lastMessageAsJson(ObjectMapperUtils.writeJsonOrDefault(request, null))
          .build();
      if (request.getIntegrationId().equals(credentials.getIntegrationId())) {
        synchronizeBooking(syncRequest, credentials);
      } else {
        log.error("Integration not setup");
      }
    } else {
      log.error("Mews credential not setup");
    }
  }

  @Override
  public List<Booking> synchronizeBooking(MewsSyncReservationRequest request, MewsCredentialResponse credentials) {
    MewsFetchingReservationResponse mewsResponse =
        fetchDetailReservation(request.getEnterpriseId(), request.getReservationIds(), request.getNumbers(), credentials);
    List<MewsReservationRequest> reservations = mewsResponse.getReservations();

    if (CollectionUtils.isEmpty(reservations)) {
      log.info("Reservations not found in Mews");
      return Collections.emptyList();
    }

    List<Booking> bookings = mewsBookingService.synchronizeBooking(mewsResponse, request.getLastMessageAsJson(), credentials);
    if (CollectionUtils.isEmpty(bookings)) {
      log.error("No reservations to synchronize");
      return Collections.emptyList();
    } else {
      List<MewsCustomerResponse> customers = mewsResponse.getCustomers();
      List<MewsNotesResponse> notes = mewsResponse.getNotes();
      
      for (Booking booking : bookings) {
        MewsReservationRequest reservation = reservations.stream().filter(r -> booking.getExternalId().equals(r.getId())).findFirst().orElse(null);
        mewsBookingItemService.saveBookingItems(booking, reservation, mewsResponse);
        mewsBookingCustomerService.saveBookingCustomers(booking, reservation, customers);
        mewsBookingNoteService.saveNotes(notes);
      }
    }

    return bookings;
  }

  private MewsFetchingReservationResponse fetchDetailReservation(String enterpriseId, List<String> reservationIds, List<String> numbers, MewsCredentialResponse credentials) {
    Map<String, Boolean> extent = new HashMap<>();
    extent.put("BusinessSegments", true);
    extent.put("Items", true);
    extent.put("Customers", true);
    extent.put("Reservations", true);
    extent.put("Products", true);
    extent.put("Services", true);
    extent.put("Notes", true);
    extent.put("Resources", true);
    extent.put("ResourceCategories", true);
    extent.put("ResourceCategoryAssignments", true);

    MewsReservationSearchRequest searchRequest = MewsReservationSearchRequest.builder()
        .reservationIds(reservationIds)
        .numbers(numbers)
        .accessToken(credentials.getAccessToken())
        .clientToken(clientToken)
        .extent(extent)
        .build();

    logger.error("mews serach request:");
    logger.error(new Gson().toJson(searchRequest));

    RestRequest<MewsReservationSearchRequest> restRequest = new RestRequest<>(mewsAllReservationUrl, searchRequest);
    MewsFetchingReservationResponse response = RestTemplateUtils.post(restRequest, MewsFetchingReservationResponse.class);

    try {
      logger.error("fetch detailed response from mews: ");
      logger.error(new Gson().toJson(response));
    } catch (Exception e) {
      e.printStackTrace();
      log.error("error in generating gson response from mews", e);
    }

    return response;
  }

  
}
