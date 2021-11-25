package com.pn.booking.model.dto.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MewsEventDiscriminator {

  SERVICE_ORDER_UPDATED,
  RESOURCE_UPDATED;

  @JsonValue
  public String getValue() {
    switch (this) {
      case SERVICE_ORDER_UPDATED:
        return "ServiceOrderUpdated";
      case RESOURCE_UPDATED:
        return "ResourceUpdated";
    }
    return null;
  }
}
