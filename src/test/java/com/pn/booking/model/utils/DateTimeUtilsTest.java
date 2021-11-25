package com.pn.booking.model.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

  private String isoStr;

  @BeforeEach
  void setUp() {
    this.isoStr = "2019-12-13T14:42:52Z";
  }

  @Test
  void parseZonedDateTime() {
    ZonedDateTime zonedDateTime = DateTimeUtils.parseZonedDateTime(isoStr);
    assertNotNull(zonedDateTime);
  }

  @Test
  void getTimestamp() {
    assertEquals(DateTimeUtils.getTimestamp(isoStr), 1576248172000l);
  }

  @Test
  void getTimeZone() {
    assertEquals(DateTimeUtils.getTimeZone(isoStr), "Z");
  }
}
