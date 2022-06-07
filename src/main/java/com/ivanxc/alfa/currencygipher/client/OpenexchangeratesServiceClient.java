package com.ivanxc.alfa.currencygipher.client;

import com.ivanxc.alfa.currencygipher.config.OpexexchangeratesClientErrorDecoder;
import com.ivanxc.alfa.currencygipher.dto.CurrencyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "opexexchangerates-service", url = "${application.openexchangerates.service.url}",
    configuration = OpexexchangeratesClientErrorDecoder.class)
public interface OpenexchangeratesServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/historical/{date}.json",
        params = "app_id=${exchangeAppId}")
    CurrencyDto findByDateAndSymbol(@PathVariable("date") String date,
        @RequestParam("symbols") String symbol, @RequestParam("app_id") String appId);
}
