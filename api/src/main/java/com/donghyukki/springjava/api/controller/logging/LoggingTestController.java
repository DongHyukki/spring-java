package com.donghyukki.springjava.api.controller.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoggingTestController {

    private final Logger logger = LoggerFactory.getLogger(LoggingTestController.class);

    @PostMapping("/api/logging/test")
    public ResponseEntity<Map<String, String>> CheckLoggingFilter(@RequestBody Map<?, ?> body) {
        var mdcMap = MDC.getCopyOfContextMap();
        logger.info(body.toString());
        return new ResponseEntity<>(mdcMap, HttpStatus.OK);
    }
}
