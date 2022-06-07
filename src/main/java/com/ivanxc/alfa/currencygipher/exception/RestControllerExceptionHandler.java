package com.ivanxc.alfa.currencygipher.exception;

import com.ivanxc.alfa.currencygipher.response.ErrorResponse;
import feign.FeignException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GiphyImageObjectException.class)
    public ResponseEntity<ErrorResponse> handleGiphyImageObjectException(GiphyImageObjectException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RatioNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleRatioNotAvailableException(RatioNotAvailableException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestApiServerException.class)
    public ResponseEntity<ErrorResponse> handleRestApiServerException(RestApiServerException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException ex) {
        List<String> details = new ArrayList<>();
        details.add((ex.contentUTF8()));
        return new ResponseEntity<>(new ErrorResponse("Incorrect request", details), HttpStatus.BAD_REQUEST);
    }

}