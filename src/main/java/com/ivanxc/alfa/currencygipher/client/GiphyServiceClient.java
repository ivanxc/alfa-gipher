package com.ivanxc.alfa.currencygipher.client;

import com.ivanxc.alfa.currencygipher.dto.GifDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "giphy-service", url = "${application.giphy.service.url}")
public interface GiphyServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/random")
    GifDto getRandomGifByWord(@RequestParam("tag") String tag, @RequestParam("api_key") String key);
}
