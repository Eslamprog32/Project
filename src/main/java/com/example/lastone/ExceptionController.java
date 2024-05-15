/*
package com.example.lastone;

import ch.qos.logback.classic.util.LogbackMDCAdapter;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(org.springframework.validation.BindException.class)
    ResponseEntity<HashMap<String, List<String>>> bindException(BindException ex){
        List<String> errors = ex.getAllErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        HashMap<String,List<String>> errMap = new HashMap<>();
        errMap.put("errors",errors);
        return new ResponseEntity<>(errMap, HttpStatus.BAD_REQUEST);
    }
}

 */
