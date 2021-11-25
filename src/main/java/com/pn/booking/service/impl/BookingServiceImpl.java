package com.pn.booking.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.pn.booking.client.feign.ConnectorClient;
import com.pn.booking.common.utils.CommonUtils;
import com.pn.booking.common.utils.MewsUtils;
import com.pn.booking.config.oauth2.ContextUser;
import com.pn.booking.config.oauth2.ContextUserService;
import com.pn.booking.model.constant.BookingStatus;
import com.pn.booking.model.dto.mapper.BookingMapper;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.BookingNoteRequest;
import com.pn.booking.model.dto.request.BookingRequest;
import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.filter.BookingFilterRequest;
import com.pn.booking.model.dto.request.mews.MewsCancelReservationRequest;
import com.pn.booking.model.dto.request.mews.MewsConfirmReservationRequest;
import com.pn.booking.model.dto.request.mews.MewsProcessReservationRequest;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest.State;
import com.pn.booking.model.dto.request.mews.MewsStartReservationRequest;
import com.pn.booking.model.dto.request.mews.MewsSyncReservationRequest;
import com.pn.booking.model.dto.response.ApplicationResponse;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.RestPageResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.model.entity.BookingCustomer;
import com.pn.booking.model.entity.BookingHasCustomer;
import com.pn.booking.model.entity.Customer;
import com.pn.booking.model.entity.CustomerProperty;
import com.pn.booking.model.utils.RestTemplateUtils;
import com.pn.booking.model.utils.RestTemplateUtils.RestRequest;
import com.pn.booking.repository.BookingHasCustomerRepository;
import com.pn.booking.repository.BookingRepository;
import com.pn.booking.service.BookingItemService;
import com.pn.booking.service.BookingNoteService;
import com.pn.booking.service.BookingService;
import com.pn.booking.service.CustomerService;
import com.pn.booking.service.MewsWebhookService;
import com.pn.booking.service.WebhookService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookingServiceImpl extends ContextService implements BookingService {

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingHasCustomerRepository bookingHasCustomerRepository;

  @Autowired
  private CustomerService customerService;

  @Autowired
  private BookingItemService itemService;

  @Autowired
  private BookingNoteService noteService;

  @Autowired
  private ConnectorClient connectorClient;

  @Autowired
  private MewsWebhookService mewsWebhookService;

  @Autowired
  private WebhookService webhookService;

  @Value("${mews.client-token}")
  private String clientToken;

  @Value("${mews.confirm-reservation-url}")
  private String mewsConfirmReservationUrl;

  @Value("${mews.start-reservation-url}")
  private String mewsStartReservationUrl;

  @Value("${mews.process-reservation-url}")
  private String mewsProcessReservationUrl;

  @Value("${mews.cancel-reservation-url}")
  private String mewsCancelReservationUrl;

  @Autowired
  private ContextUserService contextUserService;
  
  @Override
  public BookingResponse get(Long bookingId) {
    log.info("Getting booking by id {}", bookingId);
    Booking booking = getEntity(bookingId);
    if (booking.getSource() != null && booking.getSource().equals(Source.MEWS.name())) {
      try {
        synchronizeMewsBooking(booking);
      } catch (Exception e) {
        // ignore if mews is unavailable
        e.printStackTrace();
      }
      return BookingMapper.INSTANCE.toResponse(getEntity(bookingId));
    } else if (booking.getClientId() != null) {
      // get application name from ms-connector
      log.error("client id: " + booking.getClientId());
      RestPageResponse<ApplicationResponse> response = connectorClient.getApplications(1, 0, null, null, null, new String[]{ booking.getClientId() })
        .getBody().getData();

      log.error("aplpication response size: " + response.getSize());

      if (response != null) {
        List<ApplicationResponse> list = response.getContent();
        if (list != null && list.size() > 0) {
          ApplicationResponse application = list.get(0);
          booking.setSource(application.getDisplayName());
        }
      }
    }
    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public BookingResponse create(BookingRequest request) {
    log.info("Creating new booking {}", request.getBookingNumber());
    Booking booking = BookingMapper.INSTANCE.toEntity(request);
    
    ContextUser user = contextUserService.getContextUser();
    if (user != null) {
      booking.setClientId(user.getClientId());
    }

    // this means a pouch connect app was used for create booking
    Boolean allowSaving = true;
    if (user != null && request.getSource() == null) {
      allowSaving = connectorClient.isActivated(
        request.getBusinessId(), 
        request.getBusinessType(), 
        user.getClientId()
      ).getBody().getData();
    }

    if (allowSaving) {
      booking = bookingRepository.save(booking);

      if (request.getCustomers() != null) {
        customerService.create(booking, request.getCustomers());
      }
      
      if (request.getItems() != null) {
        itemService.create(booking, request.getItems());
      }

      if (request.getNotes() != null) {
        for (BookingNoteRequest bookingNote : request.getNotes()) {
          bookingNote.setBookingId(booking.getId());
        }
      }
      
      noteService.createOrUpdate(request.getNotes());

      webhookService.fireWebhook("event.created", request.getBusinessId(), request.getBusinessType());

      return BookingMapper.INSTANCE.toResponse(booking);
    } else {
      throw new IllegalArgumentException("Not allowed to make modifications for this businessId and businessType");
    }
  }

  @Override
  public BookingResponse update(Long bookingId, BookingRequest request) {
    log.info("Updating booking {}: {}", bookingId, request.toString());
    Booking booking = getEntity(bookingId);

    if (Source.MEWS.name().equals(booking.getSource())) {
      MewsCredentialResponse credentialResponse = connectorClient.getMewsCustomer(null, booking.getBusinessId()).getBody().getData();
      synchronizeMewsReservationState(booking, request, credentialResponse);
    }
    
    booking = BookingMapper.INSTANCE.toEntity(request);
    
    bookingRepository.save(booking);
    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public BookingResponse checkIn(Long bookingId) {
    Booking booking = bookingRepository
        .findById(bookingId)
        .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
    
    if (!booking.getStatus().equals(BookingStatus.CHECKED_IN.name())) {
      booking.setStatus(BookingStatus.CHECKED_IN.name());
      bookingRepository.save(booking);

      // if (Source.MEWS.name().equals(booking.getSource())) {
      //   MewsCredentialResponse credentialResponse = connectorClient.getMewsCustomer(null, booking.getBusinessId()).getBody().getData();
      //   synchronizeMewsReservationState(credentialResponse, booking, State.STARTED);
      // }
    }

    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public BookingResponse checkOut(Long bookingId) {
    Booking booking = bookingRepository
        .findById(bookingId)
        .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));
    
    if (!booking.getStatus().equals(BookingStatus.CHECKED_OUT.name())) {
      booking.setStatus(BookingStatus.CHECKED_OUT.name());
      bookingRepository.save(booking);

      // if (Source.MEWS.name().equals(booking.getSource())) {
      //   MewsCredentialResponse credentialResponse = connectorClient.getMewsCustomer(null, booking.getBusinessId()).getBody().getData();
      //   synchronizeMewsReservationState(credentialResponse, booking, State.PROCESSED);
      // }
    }
    
    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public List<BookingResponse> search(BookingFilterRequest request) {
    log.info("Filtering bookings: " + request.toString());
    List<Booking> bookings = bookingRepository.search(request);
    if (!StringUtils.isEmpty(request.getStartDateTime())) {
      Integer offset = CommonUtils.getOffset(request.getStartDateTime());
      for (Booking booking : bookings) {
        booking.setStartTime(CommonUtils.addHours(booking.getStartTime(), offset));
        booking.setEndTime(CommonUtils.addHours(booking.getEndTime(), offset));
      }
    }
    return BookingMapper.INSTANCE.toResponses(bookings);
  }

  @Override
  public Long count(BookingFilterRequest request) {
    log.info("Filtering bookings: " + request.toString());
    return bookingRepository.count(request);
  }

  @Override
  public BookingResponse addGuests(Long bookingId, List<CustomerRequest> guests) {
    log.info("Adding guests to booking {}: {} guest(s)", bookingId, guests.size());
    Booking booking = getEntity(bookingId);
    customerService.create(booking, guests);
    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public BookingResponse updateGuests(Long bookingId, List<CustomerRequest> guests) {
    log.info("Updating guests to booking {}: {} guest(s)", bookingId, guests.size());
    Booking booking = getEntity(bookingId);
    List<BookingHasCustomer> customers = bookingHasCustomerRepository.findAllByBookingId(bookingId);
    
    bookingHasCustomerRepository.deleteAll(customers);

    List<Customer> bookingCustomers = customerService.createOrUpdate(guests);

    List<BookingHasCustomer> bookingHasCustomer = new ArrayList<BookingHasCustomer>();
    bookingCustomers.forEach(action -> {
      BookingHasCustomer obj = new BookingHasCustomer();
      obj.setBookingId(bookingId);
      obj.setCustomersId(action.getId());
      obj.setCustomer(action);
      bookingHasCustomer.add(obj);
    });

    bookingHasCustomerRepository.saveAll(bookingHasCustomer);

    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Override
  public BookingResponse addItems(Long bookingId, List<BookingItemRequest> items) {
    log.info("Adding items to booking {}: {} item(s)", bookingId, items.size());
    Booking booking = getEntity(bookingId);
    itemService.create(booking, items);
    return BookingMapper.INSTANCE.toResponse(booking);
  }

  @Transactional
  @Override
  public List<Booking> createOrUpdate(List<BookingRequest> requestList, String source) {
    if (CollectionUtils.isEmpty(requestList)) {
      return Collections.emptyList();
    }
    List<String> externalIds = requestList.stream().map(BookingRequest::getExternalId).collect(Collectors.toList());
    List<Booking> currentBookings = bookingRepository.findBySourceAndExternalIdIn(source, externalIds);

    List<Booking> createOrUpdateBooks = new ArrayList<>();
    requestList.forEach(request -> {
      Booking existingBooking = currentBookings.stream().filter(currentBooking -> currentBooking.getExternalId().equals(request.getExternalId()))
          .findFirst().orElse(null);
      if (existingBooking != null) {
        request.setId(existingBooking.getId());
      }
      Booking booking = BookingMapper.INSTANCE.toEntity(request);
      createOrUpdateBooks.add(booking);
    });
    bookingRepository.saveAll(createOrUpdateBooks);
    return createOrUpdateBooks;
  }

  private Booking getEntity(Long bookingId) {
    Booking booking = bookingRepository
        .findById(bookingId)
        .orElseThrow(() -> new IllegalArgumentException("Booking not found: " + bookingId));

    Set<BookingCustomer> bookingCustomers = new HashSet<BookingCustomer>();
    for (BookingCustomer bookingCustomer : booking.getBookingCustomers()) {
      Set<CustomerProperty> propertiesForThisBooking = new HashSet<CustomerProperty>();
      Set<CustomerProperty> properties = bookingCustomer.getCustomer().getProperties();
      for (CustomerProperty property : properties) {
        if (property.getBookingId() != null && property.getBookingId().equals(bookingId)) {
          propertiesForThisBooking.add(property);
        }
      }
      bookingCustomer.getCustomer().setProperties(propertiesForThisBooking);
      bookingCustomers.add(bookingCustomer);
    }
    booking.setBookingCustomers(bookingCustomers);

    return booking;
  }

  private void synchronizeMewsReservationState(Booking booking, BookingRequest request, MewsCredentialResponse credentials) {
    if (StringUtils.isBlank(booking.getExternalId())) {
      log.info("Booking {} does not have externalId to update MEWS reservation", booking.getId());
      return;
    }

    if (!shouldUpdatingMewsReservationState(booking.getStatus(), request.getStatus())) {
      log.info("Do not update mews reservation state of bookingId: {}. Old state is {}, new state is {}",
          booking.getId(), booking.getStatus(), request.getStatus());
      return;
    }
    
    if (credentials != null) {
      log.info("Not found Mews credential for {}", username());
      return;
    }

    State newState = MewsUtils.denormalizeMewsBookingStatus(BookingStatus.valueOf(request.getStatus()));
    if (newState != null) {
      synchronizeMewsReservationState(credentials, booking, newState);
    }
    
    log.info("Update state for bookingId {} to Mews is success", booking.getId());
  }

  private void synchronizeMewsReservationState(MewsCredentialResponse credentialResponse, Booking booking, State newState) {
    String accessToken = credentialResponse.getAccessToken();
    switch (newState) {
      case CONFIRMED:
        // confirm reservation
        confirmMewsReservation(booking.getExternalId(), accessToken, clientToken);
        break;
      case STARTED:
        // check-in reservation
        startMewsReservation(booking.getExternalId(), accessToken, clientToken);
        break;
      case PROCESSED:
        // check-out reservation
        processMewsReservation(booking.getExternalId(), accessToken, clientToken);
        break;
      case CANCELED:
        // cancel reservation
        cancelMewsReservation(booking.getExternalId(), accessToken, clientToken);
        break;
      default:
        break;
    }
  }

  private void confirmMewsReservation(String reservationId, String accessToken, String clientToken) {
    MewsConfirmReservationRequest request = MewsConfirmReservationRequest.builder()
        .accessToken(accessToken)
        .clientToken(clientToken)
        .reservationIds(Collections.singletonList(reservationId))
        .build();
    RestRequest<MewsConfirmReservationRequest> restRequest = new RestRequest<>(mewsConfirmReservationUrl, request);
    RestTemplateUtils.post(restRequest, Void.class);
  }

  private void startMewsReservation(String reservationId, String accessToken, String clientToken) {
    MewsStartReservationRequest request = MewsStartReservationRequest.builder()
        .accessToken(accessToken)
        .clientToken(clientToken)
        .reservationId(reservationId)
        .build();
    RestRequest<MewsStartReservationRequest> restRequest = new RestRequest<>(mewsStartReservationUrl, request);
    RestTemplateUtils.post(restRequest, Void.class);
  }

  private void processMewsReservation(String reservationId, String accessToken, String clientToken) {
    MewsProcessReservationRequest request = MewsProcessReservationRequest.builder()
        .accessToken(accessToken)
        .clientToken(clientToken)
        .reservationId(reservationId)
        .build();
    RestRequest<MewsProcessReservationRequest> restRequest = new RestRequest<>(mewsProcessReservationUrl, request);
    RestTemplateUtils.post(restRequest, Void.class);
  }

  private void cancelMewsReservation(String reservationId, String accessToken, String clientToken) {
    MewsCancelReservationRequest request = MewsCancelReservationRequest.builder()
        .accessToken(accessToken)
        .clientToken(clientToken)
        .reservationIds(Collections.singletonList(reservationId))
        .note("Cancellation through Connector API")
        .build();
    RestRequest<MewsCancelReservationRequest> restRequest = new RestRequest<>(mewsCancelReservationUrl, request);
    RestTemplateUtils.post(restRequest, Void.class);
  }

  private boolean shouldUpdatingMewsReservationState(String oldStatus, String newStatus) {
    State oldState = State.getState(oldStatus);
    State newState = State.getState(newStatus);
    return (oldState == State.OPTIONAL && newState == State.CONFIRMED)
        || (oldState == State.CONFIRMED && newState == State.STARTED)
        || (oldState == State.STARTED && newState == State.PROCESSED)
        || (newState == State.CANCELED);
  }

  @Override
  public Long countToBeCheckedIn(BookingFilterRequest request) {
    return bookingRepository.countToBeCheckedIn(request);
  }

  @Override
  public Long countToBeCheckedOut(BookingFilterRequest request) {
    return bookingRepository.countToBeCheckedOut(request);
  }

  // private boolean shouldSynchronizeToMews(Page<Booking> result, BookingFilterRequest filter) {
  //   return result.getTotalElements() == 0
  //       && Source.MEWS.name().equalsIgnoreCase(filter.getSource());
  // }

  private void synchronizeMewsBooking(Booking booking) {
    MewsCredentialResponse credentials = connectorClient.getMewsCustomer(null, booking.getBusinessId()).getBody().getData();
    MewsSyncReservationRequest syncRequest = MewsSyncReservationRequest.builder()
          .enterpriseId(credentials.getEnterpriseId())
          .reservationIds(Arrays.asList(booking.getExternalId()))
          //.lastMessageAsJson(ObjectMapperUtils.writeJsonOrDefault(booking.getLastMessage(), null))
          .build();

    if (credentials != null) {
      mewsWebhookService.synchronizeBooking(syncRequest, credentials);
    }
  }
}
