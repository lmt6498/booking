
package com.pn.booking.controller;

import com.pn.booking.model.dto.RestResult;
import com.pn.booking.model.dto.request.CustomerRequest;
import com.pn.booking.model.dto.request.filter.GuestFilterRequest;
import com.pn.booking.model.dto.response.CustomerResponse;
import com.pn.booking.model.dto.response.BookingResponse;
import com.pn.booking.model.dto.response.FilterResponse;
import com.pn.booking.service.CustomerService;
import com.pn.booking.service.BookingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings/{bookingId}/customer")
public class CustomerController extends BaseController {

  @Autowired
  private BookingService bookingService;

  @Autowired
  private CustomerService bookingCustomerService;

  @GetMapping
  public ResponseEntity<RestResult<List<CustomerResponse>>> search(
      @PathVariable Long bookingId,
      @RequestParam(required = false, defaultValue = "20") Integer size,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "id ASC") String[] orders
  ) {
    GuestFilterRequest filter = GuestFilterRequest.builder()
        .page(page)
        .size(size)
        .orders(orders)
        .bookingId(bookingId)
        .build();
    FilterResponse<CustomerResponse> result = bookingCustomerService.filter(filter);
    return new ResponseEntity<>(
        new RestResult<List<CustomerResponse>>()
            .message("Request success")
            .data(result.getData())
            .metadata(result.paginationMap()),
        HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<RestResult<BookingResponse>> addGuestsToBooking(
      @PathVariable Long bookingId,
      @RequestBody List<CustomerRequest> guests
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingService.addGuests(bookingId, guests))
            .message("Add guest to booking success"),
        HttpStatus.OK
    );
  }

  @PostMapping("/update")
  public ResponseEntity<RestResult<BookingResponse>> updateCustomers(
      @PathVariable Long bookingId,
      @RequestBody List<CustomerRequest> guests
  ) {
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .data(bookingService.updateGuests(bookingId, guests))
            .message("Update guest in booking success"),
        HttpStatus.OK
    );
  }

  @DeleteMapping("/remove")
  public ResponseEntity<RestResult> removeCustomers(
      @PathVariable Long bookingId,
      @RequestBody List<Long> customerIds
  ) {
    bookingCustomerService.removeInBooking(bookingId, customerIds);
    return new ResponseEntity<>(
        new RestResult<BookingResponse>()
            .status(RestResult.STATUS_SUCCESS)
            .message("Remove guest in booking success"),
        HttpStatus.OK
    );
  }

}
