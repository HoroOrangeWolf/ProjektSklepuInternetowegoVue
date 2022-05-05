package com.computer.parts.shop.Handlers;

import com.computer.parts.shop.Exceptions.BadRequestException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@ControllerAdvice
public class GlobalBadRequestHandler {

  @ExceptionHandler(value = { BadRequestException.class })
  public ResponseEntity<Map<String, String>> generateBadRequestException(
    BadRequestException badRequestException
  ) {
    Map<String, String> stringStringMap = new HashMap<>();

    stringStringMap.put("message", badRequestException.getMessage());
    stringStringMap.put("status", HttpStatus.BAD_REQUEST + "");

    return new ResponseEntity<>(stringStringMap, HttpStatus.BAD_REQUEST);
  }
}
