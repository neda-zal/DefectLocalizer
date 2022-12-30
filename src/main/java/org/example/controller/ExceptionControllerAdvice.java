package org.example.controller;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionControllerAdvice {

  @ExceptionHandler({Exception.class})
  public ResponseEntity<ErrorResponse> error(Exception ex) throws Exception {
    log.error(ex.getMessage(), ex);
    if (ex instanceof AccessDeniedException) {
      throw ex;
    }
    return new ResponseEntity<>(
        new ErrorResponse("Internal Error", 500, ex.getMessage(), new Date().getTime()),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class ErrorResponse {

    private String message;
    private int code;
    private String moreInfo;
    private long timestamp;
  }

}
