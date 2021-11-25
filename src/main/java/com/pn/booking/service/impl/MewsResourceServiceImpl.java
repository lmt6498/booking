package com.pn.booking.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.pn.booking.model.dto.request.mews.MewsResourcesSearchRequest;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsResourceListResponse;
import com.pn.booking.model.utils.RestTemplateUtils;
import com.pn.booking.model.utils.RestTemplateUtils.RestRequest;
import com.pn.booking.service.MewsResourceService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MewsResourceServiceImpl implements MewsResourceService {
  
  @Value("${mews.client-token}")
  private String clientToken;

  @Value("${mews.get-all-resources-url}")
  private String getAllResourcesUrl;

  @Override
  public MewsResourceListResponse fetchResources(MewsCredentialResponse credentials) {
    Map<String, Boolean> extent = new HashMap<>();
    extent.put("Resources", true);
    extent.put("ResourceCategories", true);
    extent.put("ResourceCategoryAssignments", true);
    
    MewsResourcesSearchRequest searchRequest = MewsResourcesSearchRequest.builder()
        .extent(extent)
        .accessToken(credentials.getAccessToken())
        .clientToken(clientToken)
        .build();

    RestRequest<MewsResourcesSearchRequest> restRequest = new RestRequest<>(getAllResourcesUrl, searchRequest);
    MewsResourceListResponse response = RestTemplateUtils.post(restRequest, MewsResourceListResponse.class);
    return response;
  }

  @Override
  public MewsResourceListResponse fetchResourceCategories(MewsCredentialResponse credentials) {
    Map<String, Boolean> extent = new HashMap<>();
    extent.put("ResourceCategories", true);
    
    MewsResourcesSearchRequest searchRequest = MewsResourcesSearchRequest.builder()
        .extent(extent)
        .accessToken(credentials.getAccessToken())
        .clientToken(clientToken)
        .build();

    RestRequest<MewsResourcesSearchRequest> restRequest = new RestRequest<>(getAllResourcesUrl, searchRequest);
    MewsResourceListResponse response = RestTemplateUtils.post(restRequest, MewsResourceListResponse.class);
    return response;
  }

}
