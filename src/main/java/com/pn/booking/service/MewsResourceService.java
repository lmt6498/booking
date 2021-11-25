package com.pn.booking.service;

import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.MewsResourceListResponse;

public interface MewsResourceService {
  public MewsResourceListResponse fetchResources(MewsCredentialResponse credentials);
  public MewsResourceListResponse fetchResourceCategories(MewsCredentialResponse credentials);
}
