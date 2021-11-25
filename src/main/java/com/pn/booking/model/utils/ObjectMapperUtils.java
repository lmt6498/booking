package com.pn.booking.model.utils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class ObjectMapperUtils {

  public static <T> T convertMapValue(Map<String, Object> map, Class<T> clazz) {
    return new ObjectMapper().convertValue(map, clazz);
  }

  public static <T> T convertInputStreamValue(InputStream input, Class<T> clazz) throws IOException, JsonParseException, JsonMappingException {
    return new ObjectMapper().readValue(input, clazz);
  }

  public static String writeJsonOrDefault(Object object, String defaultValue) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      return defaultValue;
    }
  }
}
