package com.pn.booking.service.impl;

import com.pn.booking.model.dto.mapper.BookingItemMapper;
import com.pn.booking.model.dto.request.BookingItemPropertyRequest;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.filter.ItemFilterRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.model.dto.response.builder.director.FilterResponseBuilderDirector;
import com.pn.booking.model.entity.Booking;
import com.pn.booking.model.entity.BookingItem;
import com.pn.booking.model.jpa.spec.FilterSpecBuilder;
import com.pn.booking.model.jpa.spec.director.ItemFilterSpecDirector;
import com.pn.booking.repository.BookingItemRepository;
import com.pn.booking.repository.BookingRepository;
import com.pn.booking.service.BookingItemPropertyService;
import com.pn.booking.service.BookingItemService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.JavaConstant.Dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class BookingItemServiceImpl implements BookingItemService {

  @Autowired
  private BookingItemRepository repository;

  @Autowired
  private BookingRepository bookingRepository;

  @Autowired
  private BookingItemPropertyService bookingItemPropertyService;

  @Override
  public BookingItemResponse get(Long bookingItemId) {
    log.info("Getting booking item by id {}", bookingItemId);
    return BookingItemMapper.INSTANCE.toResponse(getEntity(bookingItemId));
  }

  @Transactional
  @Override
  public List<BookingItemResponse> create(Booking booking, List<BookingItemRequest> requestList) {
    log.info("Creating {} items for booking {}", requestList.size(), booking.getId());
    if (CollectionUtils.isEmpty(requestList)) {
      return Collections.emptyList();
    }
    List<BookingItem> bookingItems = BookingItemMapper.INSTANCE.toEntities(requestList);
    if (!CollectionUtils.isEmpty(bookingItems)) {
      bookingItems.forEach(item -> item.setBookingId(booking.getId()));
      repository.saveAll(bookingItems);
    }
    return bookingItems.stream()
        .map(bookingItem -> BookingItemMapper.INSTANCE.toResponse(bookingItem))
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public List<BookingItemResponse> addItemToBooking(Long bookingId, List<BookingItemRequest> requestList)  {
  
    if (CollectionUtils.isEmpty(requestList)) {
      return Collections.emptyList();
    }

    Booking booking = bookingRepository.findFirstById(bookingId);
    log.info("Creating {} items for booking {}", requestList.size(), booking.getId());
    List<BookingItem> bookingItems = BookingItemMapper.INSTANCE.toEntities(requestList);

    if (!CollectionUtils.isEmpty(bookingItems)) {
      bookingItems.forEach(item -> item.setBookingId(booking.getId()));
      repository.saveAll(bookingItems);
    }

    return bookingItems.stream()
        .map(bookingItem -> BookingItemMapper.INSTANCE.toResponse(bookingItem))
        .collect(Collectors.toList());

  }

  @Transactional
  @Override
  public BookingItemResponse update(Long bookingId, BookingItemRequest request) {
    Long bookingItemId = request.getId();
    log.info("Update {} item for booking {}", bookingItemId, bookingId);

    BookingItem bookingItem = repository.findFirstById(request.getId());

    if (bookingItem == null || !bookingItem.getBookingId().equals(bookingId)) {
      throw new AccessDeniedException("Could not access item " + bookingItemId + " of booking " + bookingId);
    }

    if (request.getItemAmount() != null) {
      bookingItem.setItemAmount(request.getItemAmount());
    }
    if (request.getName() != null) {
      bookingItem.setName(request.getName());
    }
    if (request.getType() != null) {
      bookingItem.setType(request.getType());
    }
    if (request.getCurrency() != null) {
      bookingItem.setCurrency(request.getCurrency());
    }
    if (request.getQuantity() != null) {
      bookingItem.setQuantity(request.getQuantity());
    }
    if (request.getTotalAmount() != null) {
      bookingItem.setTotalAmount(request.getTotalAmount());
    }
    if (request.getProperties().size() != 0 && request.getProperties() != null) {
      List<BookingItemPropertyRequest> properties = new ArrayList<>();
      List<Long> toRemove = new ArrayList<>();
      request.getProperties().forEach(property -> {
        if (!property.getRemove()) {
          properties.add(property);
        } else {
          toRemove.add(property.getId());
        }
      });
      bookingItemPropertyService.createOrUpdate(properties);
      bookingItemPropertyService.deleteProperties(toRemove);
    }

    repository.save(bookingItem);

    return BookingItemMapper.INSTANCE.toResponse(bookingItem);
  }

  @Override
  public FilterResponse<BookingItemResponse> filter(ItemFilterRequest filter) {
    log.info("Filtering items: " + filter.toString());
    FilterSpecBuilder<BookingItem> filterSpec = new ItemFilterSpecDirector(filter).build();
    Page<BookingItem> result = repository.findAll(filterSpec.getSpec(), filterSpec.getPageable());

    FilterResponse<BookingItemResponse> filterResponse = new FilterResponseBuilderDirector<BookingItemResponse>(result).buildPagination();
    filterResponse.setData(
        result.toList().stream()
            .map(item -> BookingItemMapper.INSTANCE.toResponse(item))
            .collect(Collectors.toList())
    );
    return filterResponse;
  }

  @Transactional
  @Override
  public BookingItemResponse remove(Long bookingId, Long bookingItemId) {
    log.info("Removing item {} of booking {}", bookingItemId, bookingId);
    BookingItem item = getEntity(bookingItemId);
    if (!item.getBookingId().equals(bookingId)) {
      throw new AccessDeniedException("Could not access item " + bookingItemId + " of booking " + bookingId);
    }
    this.repository.delete(item);
    return BookingItemMapper.INSTANCE.toResponse(item);
  }

  @Transactional
  @Override
  public void removeAllByBookingId(Long bookingId) {
    List<BookingItem> items = repository.findByBookingId(bookingId);
    repository.deleteAll(items);
  }

  @Transactional
  @Override
  public Map<Long, List<BookingItemResponse>> forceCreate(Map<Booking, List<BookingItemRequest>> requestMap) {
    if (CollectionUtils.isEmpty(requestMap)) {
      log.info("No items to create");
      return Collections.emptyMap();
    }
    Set<Long> bookingIds = requestMap.keySet().stream().map(Booking::getId).collect(Collectors.toSet());
    List<BookingItem> currentItems = repository.findByBookingIdIn(bookingIds);
    if (!CollectionUtils.isEmpty(currentItems)) {
      repository.deleteInBatch(currentItems);
    }

    List<BookingItem> creatingItems = new ArrayList<>();
    requestMap.forEach((booking, itemRequestList) -> itemRequestList.forEach(itemRequest -> {
      BookingItem item = BookingItemMapper.INSTANCE.toEntity(itemRequest);
      item.setBookingId(booking.getId());
      creatingItems.add(item);
    }));
    log.info("Force creating {} items", creatingItems.size());
    repository.saveAll(creatingItems);

    List<BookingItemResponse> responses = BookingItemMapper.INSTANCE.toResponses(creatingItems);
    return responses.stream().collect(Collectors.groupingBy(BookingItemResponse::getBookingId));
  }

  private BookingItem getEntity(Long bookingItemId) {
    return repository
        .findById(bookingItemId)
        .orElseThrow(() -> new IllegalArgumentException("Booking item not found: " + bookingItemId));
  }

}
