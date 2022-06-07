package com.ivanxc.alfa.currencygipher.config;

import com.ivanxc.alfa.currencygipher.exception.RatioNotAvailableException;
import com.ivanxc.alfa.currencygipher.exception.RestApiServerException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

public class OpexexchangeratesClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus responseStatus = HttpStatus.valueOf(response.status());

        if (responseStatus.is5xxServerError()) {
            return new RestApiServerException("External service returned an internal error.");
        } else if (responseStatus.equals(HttpStatus.BAD_REQUEST)) {
            return new RatioNotAvailableException("Historical rates for the current date are not "
                + "available - please try later.");
        } else {
            return new Exception("Unknown exception.");
        }
    }
}
