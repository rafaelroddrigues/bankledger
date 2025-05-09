package com.bankledger.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExceptionList.class)
    public ResponseEntity<Map<String, Object>> handleExceptionList(ExceptionList ex, WebRequest request) {
        return buildErrorResponse(ex.getErrors(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex, WebRequest request) {
        Map<String, List<String>> errors = new LinkedHashMap<>();
        errors.put("error", List.of(ex.getMessage()));
        return buildErrorResponse(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(Map<String, List<String>> errors, HttpStatus status) {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("status", status.value());
        errorDetails.put("errors", errors);
        return new ResponseEntity<>(errorDetails, status);
    }
}