package com.ivanxc.alfa.currencygipher.response;

import java.util.List;
import lombok.Data;

@Data
public class ErrorResponse {
    private final String message;
    private final List<String> details;
}
