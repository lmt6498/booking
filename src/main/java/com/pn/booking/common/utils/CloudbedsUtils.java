package com.pn.booking.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;


@Slf4j
public class CloudbedsUtils {
    public static Timestamp convertToTimeStamp(String dateString, String pattern){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
            Date parsedDate = dateFormat.parse(dateString);
            return new java.sql.Timestamp(parsedDate.getTime());
        } catch(Exception e) { //this generic but you can control another types of exception
            log.info("Cannot parsing date with exception {}", e.getMessage());
            log.error("Cannot parsing date with exception: " ,e);
            return null;
        }

    }

}
