package com.ivanxc.alfa.currencygipher.dto;

import com.ivanxc.alfa.currencygipher.exception.ResourceNotFoundException;
import java.util.Map;
import java.util.Optional;
import lombok.Data;

@Data
public class CurrencyDto {
    private long timestamp;
    private String base;
    private Map<String, Double> rates;

    public double getRatio(String symbol) {
        return Optional.ofNullable(rates.get(symbol))
            .orElseThrow(() -> new ResourceNotFoundException("Cannot found ratio for " + symbol));
    }
}
