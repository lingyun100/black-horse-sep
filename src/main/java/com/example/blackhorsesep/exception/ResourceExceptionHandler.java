package com.example.blackhorsesep.exception;

import static com.example.blackhorsesep.exception.ErrorCodeConstant.COURSE_PLATFORM_ERROR;
import static com.example.blackhorsesep.exception.ErrorCodeConstant.PARAM_VALIDATE_FAILURE;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author linyun.xie
 */
@ControllerAdvice
@Slf4j
public class ResourceExceptionHandler {

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      ConstraintViolationException ex) {
    log.error("constraint violation exception", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorResponse(PARAM_VALIDATE_FAILURE, ex.getMessage()));
  }

  @ExceptionHandler(CoursePlatformException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(
      CoursePlatformException ex) {
    log.error("course platform exception", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse(COURSE_PLATFORM_ERROR, ex.getMessage()));
  }
}
