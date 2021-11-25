package com.pn.booking.model.entity.listener;

import java.sql.Timestamp;

/**
 * Author: chautn
 */
public interface CUEntity {

  // void setUpdatedBy(String updatedBy);

  void setUpdatedAt(Timestamp updatedAt);

  // void setCreatedBy(String createdBy);

  void setCreatedAt(Timestamp createdAte);
}
