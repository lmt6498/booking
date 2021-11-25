package com.pn.booking.service.cloudbeds.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.pn.booking.client.feign.CloudbedsAPIClient;
import com.pn.booking.client.feign.ConnectorClient;
import com.pn.booking.common.BadRequestException;
import com.pn.booking.common.utils.CloudbedsUtils;
import com.pn.booking.common.utils.MewsUtils;
import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.cloudbeds.*;
import com.pn.booking.model.dto.response.cloudbeds.webhook.*;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingCustomer;
import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.repository.BookingCustomerRepository;
import com.pn.booking.repository.BookingItemRepository;
import com.pn.booking.repository.BookingRepository;
import com.pn.booking.repository.CustomerRepository;
import com.pn.booking.service.cloudbeds.CloudbedsBookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CloudbedsBookingServiceImpl implements CloudbedsBookingService {

    private final CloudbedsAPIClient cloudbedClient;
    private final ConnectorClient connectorClient;
    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final BookingCustomerRepository bookingCustomerRepository;
    private final BookingItemRepository bookingItemRepository;

    @Override
    @Transactional
    public void receiveWebhookMessage(Map<String, Object> data) {
        AbstractWebhookCloudbeds webhookData = this.convertMapWebhook(data);
        Gson gson = new Gson();
        log.debug("message received from cloudbeds:");
        log.debug(gson.toJson(webhookData));
        log.info("Get data from cloudbeds webhook type {} with data {}", webhookData.getEvent(), webhookData.getReservationId());
        MewsCredentialResponse credentialResponse = connectorClient.getCloudbedsCredentialWebhook(webhookData.getPropertyId()).getBody().getData();

        //need to refactor this to
        if (webhookData instanceof CreatedWebhooksCloudbeds) {
            this.createReservation(credentialResponse, webhookData.getReservationId());
        } else if (webhookData instanceof StatusChangedWebhook) {
            this.updateStatus(credentialResponse.getAccessToken(), webhookData.getReservationId());
        }
        //Todo: get another information of reservation then saving webhook data to db
    }


    private Boolean updateStatus(String token, String reservationId) {
        var cloudbedResponse = cloudbedClient.getReservationById(token, reservationId);
        CloudBedReservationResponse reservationResponse = new ObjectMapper().convertValue(cloudbedResponse.get("data"), CloudBedReservationResponse.class);
        Booking bookingEntity = bookingRepository.findFirstBySourceAndExternalId(Booking.Source.CLOUDBEDS.name(), reservationId)
                .orElseThrow(() -> {
                    log.info("Couldn't find reservation with id {} using webhook");
                    throw new BadRequestException("Reservation with id " + reservationId + " not found, could not update status ");
                });

        bookingEntity.setStatus(MewsUtils.normalizeCloudbedsBookingStatus(reservationResponse.getStatus()).name());
        bookingRepository.save(bookingEntity);
        return true;
    }

    @Override
    @Transactional
    public List<Booking> synchronizeBooking(Long venueId, boolean isSyncAll) {
        MewsCredentialResponse credentialResponse = connectorClient.getCloudbedsIntegrationData(venueId).getBody().getData();
        Map<String, Object> reservations = cloudbedClient.getReservations(credentialResponse.getAccessToken(), "0");
        List<CloudbedReservationCompactResponse> reservationIDs = Arrays.asList(new ObjectMapper().convertValue(reservations.get("data"), CloudbedReservationCompactResponse[].class));
        if (!isSyncAll) {
            reservationIDs = reservationIDs.stream()
                    .filter(item -> "confirmed".equals(item.getStatus()))
                    .collect(Collectors.toList());
        }
        for (var item : reservationIDs) {
            //this may cause problem after because api rate limit of cloudbed is 5 request/second
            this.createReservation(credentialResponse, item.getReservationID());
        }
        return null;
    }


    private AbstractWebhookCloudbeds convertMapWebhook(Map<String, Object> data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            switch (data.get("event").toString()) {
                //Todo: add some more case with other event like delete, date update
                case "reservation/created":
                    return mapper.convertValue(data, CreatedWebhooksCloudbeds.class);
                case "reservation/status_changed":
                    return mapper.convertValue(data, StatusChangedWebhook.class);
                case "dates_changed":
                    return mapper.convertValue(data, DateChangeWebhook.class);
                case "reservation/accommodation_status_changed":
                    return mapper.convertValue(data, AccomStatusChangeWebhook.class);
                case "reservation/deleted":
                    return mapper.convertValue(data, DeletedResWebhook.class);
            }
            log.warn("Unknown Cloudbeds data {}", mapper.writeValueAsString(data));
        } catch (Exception e) {
            log.error("Could not convert Cloudbeds data", e);
        }
        return null;
    }


    public void createReservation(MewsCredentialResponse credentialResponse, String reservationId) {
        var cloudbedResponse = cloudbedClient.getReservationById(credentialResponse.getAccessToken(), reservationId);
        CloudBedReservationResponse reservationResponse = new ObjectMapper().convertValue(cloudbedResponse.get("data"), CloudBedReservationResponse.class);
        log.debug(reservationResponse.toString());
        List<Unassigned> rooms = new ArrayList<>();
        rooms.addAll(reservationResponse.getAssigned());
        rooms.addAll(reservationResponse.getUnassigned());


        Booking bookingEntity = bookingRepository.findFirstBySourceAndExternalId(Booking.Source.CLOUDBEDS.name(), reservationId).orElse(new Booking());

        bookingEntity.setType(reservationResponse.isAnonymized() ? Booking.Type.ONLINE : Booking.Type.WALK_IN);
        bookingEntity.setStatus(MewsUtils.normalizeCloudbedsBookingStatus(reservationResponse.getStatus()).name());
        bookingEntity.setRawStatus(reservationResponse.getStatus());
        bookingEntity.setBusinessId(credentialResponse.getBusinessId());
        bookingEntity.setSource(Booking.Source.CLOUDBEDS.name());
        bookingEntity.setChildCount(Integer.parseInt(rooms.get(0).getChildren()));
        bookingEntity.setAdultCount(Integer.parseInt(rooms.get(0).getAdults()));
        bookingEntity.setEnterpriseId(credentialResponse.getEnterpriseId());
        bookingEntity.setExternalId(reservationResponse.getReservationID());
        bookingEntity.setStartTime(CloudbedsUtils.convertToTimeStamp(reservationResponse.getStartDate(), "yyyy-MM-dd"));
        bookingEntity.setEndTime(CloudbedsUtils.convertToTimeStamp(reservationResponse.getEndDate(), "yyyy-MM-dd"));
        bookingEntity.setBookedAt(CloudbedsUtils.convertToTimeStamp(reservationResponse.getDateCreated(), "yyyy-MM-dd HH:mm:ss"));

        Booking finalBookingEntity = bookingRepository.save(bookingEntity);

        this.bookingItemsFromReservationsItem(rooms, finalBookingEntity.getId());
        Set<Customer> customers = this.customersFromCloudbedsCustomer(reservationResponse.getGuestList());

        for (Customer customer : customers) {
            BookingCustomer bookingCustomer = BookingCustomer.builder()
                    .bookingId(finalBookingEntity.getId())
                    .customerId(customer.getId())
                    .build();
            bookingCustomerRepository.save(bookingCustomer);
        }
    }


    private Set<BookingItem> bookingItemsFromReservationsItem(List<Unassigned> rooms, Long bookingId) {
        Set<BookingItem> bookingItems = new HashSet<>();
        for (var item : rooms) {
            BookingItem bookingItem = bookingItemRepository.findBookingItemByExternalId(item.getSubReservationID()).orElse(new BookingItem());
            BookingItem.Status status = item instanceof Assigned ? BookingItem.Status.ASSIGNED : BookingItem.Status.UNASSIGNED;
            bookingItem.setBookingId(bookingId);
            bookingItem.setItemAmount(BigDecimal.valueOf(Double.valueOf(item.getRoomTotal())));
            bookingItem.setName(item.getRoomTypeName());
            bookingItem.setType(BookingItem.Type.PLACE);
            bookingItem.setItemStatus(status);
            bookingItem.setSource(Booking.Source.CLOUDBEDS.name());
            bookingItem.setExternalId(item.getSubReservationID());
            bookingItems.add(bookingItem);
        }
        return new HashSet<>(bookingItemRepository.saveAll(bookingItems));

    }

    private Set<Customer> customersFromCloudbedsCustomer(Map<Long, Guest> guestMap) {
        List<Customer> customers = new ArrayList<>();
        guestMap.forEach((key, value) -> {
            Customer customer = customerRepository.findFirstBySourceAndExternalId(Booking.Source.CLOUDBEDS.name(), key.toString()).orElse(new Customer());
            customer.setEmail(value.getGuestEmail());
            customer.setFirstName(value.getGuestFirstName());
            customer.setLastName(value.getGuestLastName());
            customer.setExternalId(key.toString());
            customer.setMobileNumber(value.getGuestPhone());
            customer.setSource(Booking.Source.CLOUDBEDS.name());
            customers.add(customer);
        });
        return new HashSet<>(customerRepository.saveAll(customers));
    }
}
