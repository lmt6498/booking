package com.pn.booking.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pn.booking.common.utils.MewsUtils;
import com.pn.booking.model.constant.BusinessType;
import com.pn.booking.model.dto.request.BookingRequest;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.response.MewsCompanyListResponse;
import com.pn.booking.model.dto.response.MewsCompanyResponse;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsFetchingReservationResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.model.utils.DateTimeUtils;
import com.pn.booking.service.BookingService;
import com.pn.booking.service.MewsBookingService;
import com.pn.booking.service.MewsCompanyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MewsBookingServiceImpl implements MewsBookingService {
  
  @Autowired
  private BookingService bookingService;

  @Autowired
  private MewsCompanyService mewsCompanyService;

  @Override
  public List<Booking> synchronizeBooking(MewsFetchingReservationResponse mewsResponse, String message, MewsCredentialResponse credentials) {
    List<BookingRequest> bookingRequestList = new ArrayList<>();
    for (MewsReservationRequest reservationRequest : mewsResponse.getReservations()) {
      List<String> companyIds = mewsResponse.getReservations().stream()
        .map(MewsReservationRequest::getCompanyId)
        .filter(companyId -> companyId != null)
        .distinct()
        .collect(Collectors.toList());

      MewsCompanyListResponse companyResponse = null;
      if (companyIds.size() > 0) {
        companyResponse = mewsCompanyService.fetchCompanies(credentials, companyIds);
      }
      bookingRequestList.add(convertToBooking(reservationRequest, message, credentials, companyResponse));
    }
    
    return bookingService.createOrUpdate(bookingRequestList, Source.MEWS.name());
  }
  
  private BookingRequest convertToBooking(MewsReservationRequest reservationRequest, String message, MewsCredentialResponse credentials,
  MewsCompanyListResponse companyResponse) {
    String companyName = null;
    if (companyResponse != null) {
      MewsCompanyResponse company = companyResponse.getCompanies().stream()
        .filter(r -> r.getId().equals(reservationRequest.getCompanyId())).findFirst().orElse(null);
      if (company != null) {
        companyName = company.getName();
      }
    }
    
    return BookingRequest.builder()
        .source(Source.MEWS.name())
        .externalId(reservationRequest.getId())
        .businessId(credentials.getBusinessId())
        .businessType(BusinessType.VENUE.name())
        .bookingNumber(reservationRequest.getNumber())
        .startTime(new Timestamp(DateTimeUtils.getTimestamp(reservationRequest.getStartUtc())))
        .endTime(new Timestamp(DateTimeUtils.getTimestamp(reservationRequest.getEndUtc())))
        .type(Booking.Type.ONLINE.name())
        .status(MewsUtils.normalizeMewsBookingStatus(reservationRequest.getState()))
        .adultCount(reservationRequest.getAdultCount())
        .childCount(reservationRequest.getChildCount())
        // .lastMessage(message) // remove temporarily - causes java heap space error
        .integrationCredentialId(credentials.getId())
        .enterpriseId(credentials.getEnterpriseId())
        .bookedAt(new Timestamp(DateTimeUtils.getTimestamp(reservationRequest.getCreatedUtc())))
        .cancelledAt(
            StringUtils.isEmpty(reservationRequest.getCancelledUtc()) ? null
                : new Timestamp(DateTimeUtils.getTimestamp(reservationRequest.getCancelledUtc()))
        )
        .company(companyName)
        .build();
  }

}
