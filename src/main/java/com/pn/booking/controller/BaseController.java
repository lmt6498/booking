package com.pn.booking.controller;

import com.pn.booking.model.dto.RestResult;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Author: chautn on 12/24/2020
 */
@Slf4j
public class BaseController implements ErrorController {

  @Override
  public String getErrorPath() {
    return "/error";
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<RestResult<String>> handleException(MethodArgumentNotValidException exception) {
    log.error("Caught a bad request", exception);
    List<String> messages = new ArrayList<>();
    Map<String, Object> metadata = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      metadata.put(fieldName, errorMessage);
      messages.add(error.toString());
    });
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .messages(messages)
            .metadata(metadata),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({AccessDeniedException.class})
  public ResponseEntity<RestResult<String>> handleAccessDeniedException(AccessDeniedException exception) {
    log.error("Caught a forbidden request", exception);
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .message(exception.getMessage())
            .data(exception.getMessage()),
        HttpStatus.FORBIDDEN
    );
  }

  @ExceptionHandler({RuntimeException.class})
  public ResponseEntity<RestResult<String>> handleException(RuntimeException exception) {
    log.error("Caught a bad request", exception);
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .message(exception.getMessage())
            .data(exception.getMessage()),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({IllegalArgumentException.class})
  public ResponseEntity<RestResult<String>> handleException(IllegalArgumentException exception) {
    log.error("Caught a bad request", exception);
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .message(exception.getMessage())
            .data(exception.getMessage()),
        HttpStatus.BAD_REQUEST
    );
  }

  @ExceptionHandler({InvalidBearerTokenException.class})
  public ResponseEntity<RestResult<String>> handleException(InvalidBearerTokenException exception) {
    log.error("Caught an unauthorized request", exception);
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .message(exception.getMessage())
            .data(exception.getMessage()),
        HttpStatus.UNAUTHORIZED
    );
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<RestResult<String>> handleException(Exception exception) {
    log.error("Caught a unhandled request", exception);
    return new ResponseEntity<>(
        new RestResult<String>()
            .status(RestResult.STATUS_ERROR)
            .message("Unexpected error")
            .data(exception.getMessage())
            .metadata("stackTrace", stackTraceToListMessage(exception.getStackTrace())),
        HttpStatus.INTERNAL_SERVER_ERROR
    );
  }

  private List<String> stackTraceToListMessage(StackTraceElement[] elements) {
    if (elements != null) {
      List<String> messages = new ArrayList<>();
      for (StackTraceElement element : elements) {
        messages.add(element.toString());
      }
      return messages;
    }
    return Collections.emptyList();
  }
}
