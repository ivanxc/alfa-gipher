package com.ivanxc.alfa.currencygipher.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GiphyImageObjectException extends RuntimeException {

    public GiphyImageObjectException(String message) {
        super(message);
    }
}
