package com.youlin.spider.demo.controller;

import com.youlin.spider.demo.vo.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(new ErrorMessage("ERROR0001", e.getMessage()));
    }
}
