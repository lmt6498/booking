package com.pn.booking.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.pn.booking.model.dto.request.BookingItemPropertyRequest;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.MewsFetchingReservationResponse;
import com.pn.booking.model.dto.response.MewsItemResponse;
import com.pn.booking.model.dto.response.MewsProductResponse;
import com.pn.booking.model.dto.response.MewsReservationResourceResponse;
import com.pn.booking.model.dto.response.MewsResourceCategoryAssignmentResponse;
import com.pn.booking.model.dto.response.MewsResourceCategoryResponse;
import com.pn.booking.model.dto.response.MewsServiceResponse;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.Booking.Source;
import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.entity.BookingItemProperty;
import com.pn.booking.service.BookingItemPropertyService;
import com.pn.booking.service.BookingItemService;
import com.pn.booking.service.MewsBookingItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MewsBookingItemServiceImpl implements MewsBookingItemService {
  
  @Autowired
  private BookingItemService bookingItemService;

  @Autowired
  private BookingItemPropertyService bookingItemPropertyService;

  @Override
  public List<BookingItemResponse> saveBookingItems(Booking booking, MewsReservationRequest reservation, MewsFetchingReservationResponse mewsResponse) {
    bookingItemService.removeAllByBookingId(booking.getId());
    
    List<BookingItemResponse> bookingItems = new ArrayList<BookingItemResponse>();
    List<BookingItemRequest> request = new ArrayList<BookingItemRequest>();

    // SAVE RESOURCES (ROOM)
    String assignedResourceId = reservation.getAssignedResourceId();
    MewsReservationResourceResponse resource = mewsResponse.getResources()
    .stream().filter(r -> assignedResourceId.equals(r.getId())).findFirst().orElse(null);
  
    if (resource != null) {
      BigDecimal itemAmount = BigDecimal.ZERO;
      BigDecimal totalAmount = BigDecimal.ZERO;
      String currency = null;
      Integer quantity = 0;

      MewsServiceResponse service = mewsResponse.getServices()
        .stream().filter(r -> r.getId().equals(reservation.getServiceId())).findFirst().orElse(null);

      if (service != null) {
        List<MewsItemResponse> items = mewsResponse.getItems();
        for (MewsItemResponse item : items) {
          if (item.getOrderId().equals(reservation.getId()) && item.getType().equals("ServiceRevenue")) {
            itemAmount = item.getAmount().getGrossValue();
            totalAmount = totalAmount.add(itemAmount);
            currency = item.getAmount().getCurrency();
            quantity++;
          }
        }
      }

      MewsResourceCategoryAssignmentResponse resourceCategoryAssignment = mewsResponse.getResourceCategoryAssignments()
        .stream().filter(r -> resource.getId().equals(r.getResourceId())).findFirst().orElse(null);
        
      if (resourceCategoryAssignment != null) {
        MewsResourceCategoryResponse resourceCategory = mewsResponse.getResourceCategories()
          .stream().filter(r -> resourceCategoryAssignment.getCategoryId().equals(r.getId())).findFirst().orElse(null);
        if (resourceCategory != null) {
          request.add(convertToBookingItem(booking, resourceCategory, itemAmount, totalAmount, currency, quantity));
        }
      }
    }

    // SAVE PRODUCTS
    List<MewsProductResponse> products = mewsResponse.getProducts();
    for (MewsProductResponse product : products) {
      BigDecimal itemAmount = product.getPrice().getValue();
      BigDecimal totalAmount = BigDecimal.ZERO;
      String currency = null;
      List<MewsItemResponse> items =  mewsResponse.getItems()
        .stream().filter(r -> r.getProductId() != null && r.getProductId().equals(product.getId())).collect(Collectors.toList());

      boolean isProductBought = false;
      for (MewsItemResponse item : items) {
        if (item.getOrderId().equals(reservation.getId()) && item.getType().equals("ProductRevenue")) {
          totalAmount = totalAmount.add(item.getAmount().getGrossValue());
          currency = item.getAmount().getCurrency();
          isProductBought = true;
        }
      }

      Integer quantity = 0;
      if (itemAmount.compareTo(BigDecimal.ZERO) > 0 && totalAmount.compareTo(BigDecimal.ZERO) > 0) {
        quantity = new BigDecimal(totalAmount + "").divide(new BigDecimal(itemAmount + "")).intValue();
      }
      
      if (isProductBought) {
        BookingItemRequest bookingItemRequest = convertToBookingItem(booking, product, itemAmount, totalAmount, currency, quantity);
        
        BookingItemRequest bookingItemRequestAlreadyAdded = request.stream()
          .filter(r -> r.getExternalId().equals(bookingItemRequest.getExternalId()) 
            && r.getSource().equals(bookingItemRequest.getSource()))
          .findFirst().orElse(null);

        if (bookingItemRequestAlreadyAdded == null) {
          request.add(bookingItemRequest);
        } else {
          bookingItemRequestAlreadyAdded.setQuantity(
            bookingItemRequestAlreadyAdded.getQuantity() + quantity
          );
        }
        
      }
    }
    
    // SAVE ALL BOOKING ITEMS
    List<BookingItemResponse> savedBookingItems = bookingItemService.create(booking, request);
    bookingItems.addAll(savedBookingItems);

    // SAVE BOOKING ITEM PROPERTY
    BookingItemResponse resourceBookingItem = savedBookingItems.stream().filter(r -> r.getType().equals(BookingItem.Type.PLACE.name()))
      .findFirst().orElse(null);

    if (resourceBookingItem != null) {
      List<BookingItemPropertyRequest> bookingItemProperties = new ArrayList<BookingItemPropertyRequest>();
      bookingItemProperties.add(convertToBookingItemProperty(resource, resourceBookingItem));
      bookingItemPropertyService.createOrUpdate(bookingItemProperties);  
    }

    return bookingItems;
  }

  private BookingItemRequest convertToBookingItem(Booking booking, MewsProductResponse product, BigDecimal itemAmount, BigDecimal totalAmount, String currency, Integer quantity) {
    return BookingItemRequest.builder()
      .bookingId(booking.getId())
      .source(Source.MEWS.name())
      .externalId(product.getId())
      .name(product.getName())
      .type(BookingItem.Type.PRODUCT)
      .itemAmount(itemAmount)
      .totalAmount(totalAmount)
      .currency(product.getPrice().getCurrency())
      .quantity(quantity)
      .build();
  }

  private BookingItemRequest convertToBookingItem(Booking booking, MewsResourceCategoryResponse category, 
    BigDecimal itemAmount, BigDecimal totalAmount, String currency, Integer quantity) {
    String name = category.getNames().values().stream().findFirst().orElse("Unavailable");
    return BookingItemRequest.builder()
        .bookingId(booking.getId())
        .source(Source.MEWS.name())
        .externalId(category.getId())
        .name(name)
        .type(BookingItem.Type.PLACE)
        .itemAmount(itemAmount)
        .totalAmount(totalAmount)
        .currency(currency)
        .quantity(quantity)
        .build();
  }
  
  private BookingItemPropertyRequest convertToBookingItemProperty(MewsReservationResourceResponse resource, BookingItemResponse bookingItem){
    return BookingItemPropertyRequest.builder()
      .bookingItemId(bookingItem.getId())
      .source(Source.MEWS.name())
      .externalId(resource.getId())
      .type(BookingItemProperty.Type.ASSIGNED_SPACE)
      .value(resource.getName())
      .build();
  }
}
