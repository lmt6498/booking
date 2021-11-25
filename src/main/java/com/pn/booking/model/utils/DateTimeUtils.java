package com.pn.booking.model.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;

public class DateTimeUtils {

  public static ZonedDateTime parseZonedDateTime(String isoStr) {
    return ZonedDateTime.parse(isoStr);
  }

  public static long getTimestamp(String isoStr) {
    return parseZonedDateTime(isoStr).toInstant().toEpochMilli();
  }

  public static String getTimeZone(String isoStr) {
    return parseZonedDateTime(isoStr).getZone().getId();
  }

  public static String getTimeUtc(Timestamp timestamp) {
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'");
    return df.format(timestamp);
  }
}
