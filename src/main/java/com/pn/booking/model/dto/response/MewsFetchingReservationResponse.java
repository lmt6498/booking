package com.pn.booking.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pn.booking.model.dto.request.mews.MewsReservationRequest;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MewsFetchingReservationResponse {

  @JsonProperty("BusinessSegments")
  private List<MewsBusinessSegmentsResponse> businessSegments;

  @JsonProperty("Items")
  private List<MewsItemResponse> items;

  @JsonProperty("Customers")
  private List<MewsCustomerResponse> customers;

  @JsonProperty("Reservations")
  private List<MewsReservationRequest> reservations;

  @JsonProperty("Notes")
  private List<MewsNotesResponse> notes;

  @JsonProperty("Products")
  private List<MewsProductResponse> products;
  
  @JsonProperty("Services")
  private List<MewsServiceResponse> services;

  @JsonProperty("Resources")
  private List<MewsReservationResourceResponse> resources;

  @JsonProperty("ResourceCategories")
  private List<MewsResourceCategoryResponse> resourceCategories;

  @JsonProperty("ResourceCategoryAssignments")
  private List<MewsResourceCategoryAssignmentResponse> resourceCategoryAssignments;
}
