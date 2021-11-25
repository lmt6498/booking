package com.pn.booking.controller;

import com.pn.booking.model.dto.RestResult;
import com.pn.booking.model.dto.request.BookingItemRequest;
import com.pn.booking.model.dto.request.filter.ItemFilterRequest;
import com.pn.booking.model.dto.response.BookingItemResponse;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.service.BookingItemService;
import com.pn.booking.service.BookingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings/{bookingId}/item")
public class BookingItemController extends BaseController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private BookingItemService bookingItemService;

  @GetMapping
  public ResponseEntity<RestResult<List<BookingItemResponse>>> filter(
      @PathVariable Long bookingId,
      @RequestParam(required = false, defaultValue = "20") Integer size,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "id ASC") String[] orders
  ) {
    ItemFilterRequest filter = ItemFilterRequest.builder()
        .page(page)
        .size(size)
        .orders(orders)
        .bookingId(bookingId)
        .build();
    FilterResponse<BookingItemResponse> result = bookingItemService.filter(filter);
    return new ResponseEntity<>(
        new RestResult<List<BookingItemResponse>>()
            .message("Request success")
            .data(result.getData())
            .metadata(result.paginationMap()),
        HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<RestResult<List<BookingItemResponse>>> addItemsToBooking(
      @PathVariable Long bookingId,
      @RequestBody List<BookingItemRequest> items
  ) {
    return new ResponseEntity<>(
        new RestResult<List<BookingItemResponse>>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingItemService.addItemToBooking(bookingId, items))
            .message("Add items to booking success"),
        HttpStatus.OK
    );
  }

  @PutMapping("/{bookingItemId}")
  public ResponseEntity<RestResult<BookingItemResponse>> updateABookingItem(
      @PathVariable Long bookingId,
      @PathVariable Long bookingItemId,
      @RequestBody BookingItemRequest items
  ) {

    items.setId(bookingItemId);
    
    return new ResponseEntity<>(
        new RestResult<BookingItemResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingItemService.update(bookingId, items))
            .message("Add items to booking success"),
        HttpStatus.OK
    );
  }

  @DeleteMapping("/{bookingItemId}")
  public ResponseEntity<RestResult<BookingItemResponse>> removeItemFromBooking(
      @PathVariable Long bookingId,
      @PathVariable Long bookingItemId
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingItemResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingItemService.remove(bookingId, bookingItemId))
            .message("Remove item from booking success"),
        HttpStatus.OK
    );
  }

}
