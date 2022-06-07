package com.ivanxc.alfa.currencygipher.controller;

import com.ivanxc.alfa.currencygipher.service.GifService;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CurrencyGipherRestController {

    private final GifService giphService;

    @GetMapping(value = "/exchange-rate-change", produces = "image/gif")
    public byte[] getCurrencyChangeAsGif(@RequestParam String currency) throws IOException {
        String gifURL = giphService.getGifByCurrencyRateChange(currency);
        InputStream inputStream = new URL(gifURL).openStream();
        return inputStream.readAllBytes();
    }

}