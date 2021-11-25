package com.pn.booking.model.constant;

public class DateTimeFormat {

  public static final String RFC3339_TIMESTAMPMILLI_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String RFC3339_TIMESTAMP_FORMAT_MOBILE = "yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ";
  public static final String RFC3339_TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
  public static final String RFC3339_TIMESTAMP_FORMAT_NO_ZONE = "yyyy-MM-dd HH:mm:ss";

  public final static String[] RFC_3339 = {
      RFC3339_TIMESTAMPMILLI_FORMAT,
      RFC3339_TIMESTAMP_FORMAT,
      RFC3339_TIMESTAMP_FORMAT_MOBILE,
  };
}
