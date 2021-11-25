package com.pn.booking.model.entity.listener;

import com.pn.booking.config.helper.AutowireHelper;
import com.pn.booking.config.oauth2.ContextUser;
import com.pn.booking.config.oauth2.ContextUserService;
import java.sql.Timestamp;
import java.util.Calendar;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: chautn
 */
public class CUEntityListener {

  @Autowired
  private ContextUserService contextUserService;

  @PrePersist
  public void onCreate(Object entity) {
    if (entity instanceof CUEntity) {
      // ContextUser user = user();
      Timestamp currentTimestamp = new Timestamp(Calendar.getInstance().getTimeInMillis());
      CUEntity cuEntity = (CUEntity) entity;
      cuEntity.setCreatedAt(currentTimestamp);
      cuEntity.setUpdatedAt(currentTimestamp);
    }
  }

  @PreUpdate
  public void onUpdate(Object entity) {
    if (entity instanceof CUEntity) {
      CUEntity cuEntity = (CUEntity) entity;
      // ContextUser user = user();
      // if ( user != null ) {
      //   cuEntity.setModifiedBy(user.getUsername());
      // }
      cuEntity.setUpdatedAt(new Timestamp(Calendar.getInstance().getTimeInMillis()));
    }
  }

  private ContextUser user() {
    AutowireHelper.autowire(this, this.contextUserService);
    return contextUserService.getContextUser();
  }
}
