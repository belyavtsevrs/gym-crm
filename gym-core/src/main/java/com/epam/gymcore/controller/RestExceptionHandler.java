package com.epam.gymcore.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String,Object>> handleResponse(ResponseStatusException ex, HttpServletRequest req) {

        Map<String,Object> map = Map.of(
                "timestamp", java.time.OffsetDateTime.now().toString(),
                "status", ex.getStatusCode().value(),
                "error", ex.getReason(),
                "path", req.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(map);
    }

    @ExceptionHandler
    public Map<String, String> handleError(final RuntimeException exception){
        return Map.of(
                "error",exception.getMessage()
        );
    }
}
