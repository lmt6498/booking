package com.pn.booking.common.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import com.pn.booking.common.BadRequestException;
import com.pn.booking.model.constant.DateTimeFormat;
import com.pn.booking.model.constant.ResponseMessage;

import org.springframework.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtils {

  public static Timestamp addHours(Timestamp timestamp, Integer hours) {
    Integer duration = (hours * 60 * 60) * 1000;
    timestamp.setTime(timestamp.getTime() + duration);
    return timestamp;
  }
  
  public static Timestamp convertStringToTimestampUTC(String timestamp) {
    if (StringUtils.isEmpty(timestamp)) {
      return null;
    }
    if (timestamp.indexOf(" ") > -1) {
      timestamp = timestamp.replaceAll(" ", "+");
    }
    if (timestamp.split(":").length > 3) { // +0800 for web, +08:00 for mobile.
      timestamp = replaceLast(timestamp, ":", "");
    }
    return convertStringToTimestampUsingZoneId(timestamp, ZoneId.of("UTC"));
  }

  public static Timestamp convertStringToTimestampUsingZoneId(String timestamp, ZoneId zoneId) {
    if (StringUtils.isEmpty(timestamp)) {
      return null;
    }

    Date date = parseDateString(timestamp);
    if (date == null) {
      return null;
    }

    Timestamp t = new Timestamp(date.getTime());
    ZonedDateTime zoneUtc = t.toInstant().atZone(zoneId);
    return Timestamp.from(zoneUtc.toInstant());
  }

  public static Date parseDateString(String timestamp) {
    if (StringUtils.isEmpty(timestamp)) {
      return null;
    }

    if (timestamp.contains("Z")) {
      timestamp = timestamp.replaceAll("Z$", "+0000");
    }

    log.error("timestamp: " + timestamp);

    String format = null;

    for (String formatString : DateTimeFormat.RFC_3339) {
      try {
        format = formatString;
        return new SimpleDateFormat(formatString).parse(timestamp);
      } catch (ParseException ignored) {
      }
    }

    throw new BadRequestException(ResponseMessage.INVALID_TIMESTAMP_FORMAT)
        .addAdditionalInformation(ResponseMessage.TIMESTAMP, timestamp)
        .addAdditionalInformation(ResponseMessage.FORMAT, format);

  }

  public static Integer getOffset(String timestamp) {
    if (StringUtils.isEmpty(timestamp)) {
      return 0;
    }

    long diffHours = 0;
    try {
      Timestamp utcTimestamp = convertStringToTimestampUTC(timestamp);
      Date dateUnConverted = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(timestamp);

      long diff = dateUnConverted.getTime() - utcTimestamp.getTime();
      diffHours = diff / (60 * 60 * 1000) % 24;

    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }

    return Math.toIntExact(diffHours);
  }

  public static String urlDecode(String value) {
    try {
      return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return value;
    }
    
  }

  public static String replaceLast(String string, String toReplace, String replacement) {
    int pos = string.lastIndexOf(toReplace);
    if (pos > -1) {
        return string.substring(0, pos)
             + replacement
             + string.substring(pos + toReplace.length());
    } else {
        return string;
    }
}

  
}
