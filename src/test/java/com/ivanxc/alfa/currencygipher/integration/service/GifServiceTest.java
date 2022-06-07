package com.ivanxc.alfa.currencygipher.integration.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ivanxc.alfa.currencygipher.client.GiphyServiceClient;
import com.ivanxc.alfa.currencygipher.client.OpenexchangeratesServiceClient;
import com.ivanxc.alfa.currencygipher.dto.CurrencyDto;
import com.ivanxc.alfa.currencygipher.dto.GifDataDto;
import com.ivanxc.alfa.currencygipher.dto.GifDto;
import com.ivanxc.alfa.currencygipher.dto.GifImageDto;
import com.ivanxc.alfa.currencygipher.exception.ResourceNotFoundException;
import com.ivanxc.alfa.currencygipher.service.GifService;
import feign.FeignException.FeignClientException;
import feign.Request;
import feign.Request.HttpMethod;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@RequiredArgsConstructor
class GifServiceTest {

    @Value("${application.giphy.service.tag.increase}")
    private final String increaseTag;
    @Value("${application.giphy.service.tag.decrease}")
    private final String decreaseTag;
    @Value("${application.giphy.service.tag.no-change}")
    private final String noChangeTag;
    private final String openexchangeratesAppId = "o-id";
    private final String giphyAppId = "g-id";
    private final String baseCurrency = "UAH";
    private final String quoteCurrency = "RUB";
    private final String notExistingCurrency = "ABCDEFGH";
    private final String renditionWithGifURL = "downsized";
    private final String renditionWithoutGifURL = "no-url";
    @MockBean
    private GiphyServiceClient giphyServiceClient;
    @MockBean
    private OpenexchangeratesServiceClient openexchangeratesServiceClient;
    private GifService gifService;

    Map<String, CurrencyDto> currencyDtos = new HashMap<>();
    String todayDate = LocalDate.now().toString();
    String yesterdayDate = LocalDate.now().minusDays(1).toString();
    String tomorrowDate = LocalDate.now().plusDays(1).toString();

    @BeforeEach
    void initCurrencies() {
        CurrencyDto lowBaseCurrencyDto = new CurrencyDto();
        lowBaseCurrencyDto.setRates(Map.of(baseCurrency, 20.0));
        CurrencyDto highBaseCurrencyDto = new CurrencyDto();
        highBaseCurrencyDto.setRates(Map.of(baseCurrency, 30.0));

        CurrencyDto lowQuoteCurrencyDto = new CurrencyDto();
        lowQuoteCurrencyDto.setRates(Map.of(quoteCurrency, 40.0d));
        CurrencyDto highQuoteCurrencyDto = new CurrencyDto();
        highQuoteCurrencyDto.setRates(Map.of(quoteCurrency, 60.0d));

        CurrencyDto notPresentRatioCurrencyDto = new CurrencyDto();
        notPresentRatioCurrencyDto.setRates(Collections.emptyMap());

        currencyDtos.put("lowBaseCurrencyDto", lowBaseCurrencyDto);
        currencyDtos.put("highBaseCurrencyDto", highBaseCurrencyDto);
        currencyDtos.put("lowQuoteCurrencyDto", lowQuoteCurrencyDto);
        currencyDtos.put("highQuoteCurrencyDto", highQuoteCurrencyDto);
        currencyDtos.put("notPresentRatioCurrencyDto", notPresentRatioCurrencyDto);
    }

    void mockingOpenexchangeratesServiceClientIncreaseRates() {
        Mockito.doReturn(currencyDtos.get("highQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId);
    }

    void mockingOpenexchangeratesServiceClientDecreaseRates() {
        Mockito.doReturn(currencyDtos.get("lowQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("highQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId);
    }

    void mockingOpenexchangeratesServiceClientNoChangeRates() {
        Mockito.doReturn(currencyDtos.get("highQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("highQuoteCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("lowBaseCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId);
    }

    void mockingOpenexchangeratesServiceClientNotAvailableRates() {
        FeignClientException feignClientException = new FeignClientException(400,
            "Historical rates for the requested date are not available",
            Request.create(HttpMethod.GET, "someUrl", Map.of("", Collections.EMPTY_LIST),
                null, null, null), null, null);

        Mockito.doThrow(feignClientException)
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId);;

        Mockito.doThrow(feignClientException)
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doThrow(feignClientException)
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, quoteCurrency, openexchangeratesAppId);

        Mockito.doThrow(feignClientException)
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doThrow(feignClientException)
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId);
    }

    void mockingOpenexchangeratesServiceClientNoRates() {
        Mockito.doReturn(currencyDtos.get("notPresentRatioCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("notPresentRatioCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("notPresentRatioCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(todayDate, notExistingCurrency, openexchangeratesAppId);

        Mockito.doReturn(currencyDtos.get("notPresentRatioCurrencyDto"))
            .when(openexchangeratesServiceClient)
            .findByDateAndSymbol(yesterdayDate, notExistingCurrency, openexchangeratesAppId);
    }

    @BeforeEach
    void mockingGiphyServiceClient() {
        GifDto increaseGifDto = new GifDto();
        GifDataDto increaseGifDataDto = new GifDataDto();
        GifImageDto increaseGifImageDto = new GifImageDto();

        Map<String, GifImageDto> richImages =
            Map.of(renditionWithGifURL, increaseGifImageDto, renditionWithoutGifURL, new GifImageDto());

        increaseGifImageDto.setUrl(increaseTag);
        increaseGifDataDto.setImages(richImages);
        increaseGifDto.setData(increaseGifDataDto);

        GifDto deacreaseGifDto = new GifDto();
        GifDataDto deacreaseGifDataDto = new GifDataDto();
        GifImageDto deacreaseGifImageDto = new GifImageDto();

        Map<String, GifImageDto> brokeImages =
            Map.of(renditionWithGifURL, deacreaseGifImageDto, renditionWithoutGifURL, new GifImageDto());

        deacreaseGifImageDto.setUrl(decreaseTag);
        deacreaseGifDataDto.setImages(brokeImages);
        deacreaseGifDto.setData(deacreaseGifDataDto);

        GifDto noChangeGifDto = new GifDto();
        GifDataDto noChangeGifDataDto = new GifDataDto();
        GifImageDto noChangeGifImageDto = new GifImageDto();

        Map<String, GifImageDto> noChangeImages =
            Map.of(renditionWithGifURL, noChangeGifImageDto, renditionWithoutGifURL, new GifImageDto());

        noChangeGifImageDto.setUrl(noChangeTag);
        noChangeGifDataDto.setImages(noChangeImages);
        noChangeGifDto.setData(noChangeGifDataDto);
        
        Mockito.doReturn(increaseGifDto)
            .when(giphyServiceClient).getRandomGifByWord(increaseTag, giphyAppId);
        Mockito.doReturn(deacreaseGifDto)
            .when(giphyServiceClient).getRandomGifByWord(decreaseTag, giphyAppId);
        Mockito.doReturn(noChangeGifDto)
            .when(giphyServiceClient).getRandomGifByWord(noChangeTag, giphyAppId);
    }

    @BeforeEach
    void prepare() {
        this.gifService = new GifService(openexchangeratesServiceClient, giphyServiceClient,
            openexchangeratesAppId, giphyAppId, baseCurrency, renditionWithGifURL,
            increaseTag, decreaseTag, noChangeTag);
    }

    @Test
    void getGifWhenCurrencyExistsAndRatioIncreased() {
        mockingOpenexchangeratesServiceClientIncreaseRates();
        String actual = gifService.getGifByCurrencyRateChange(quoteCurrency);
        assertEquals(actual, increaseTag);
    }

    @Test
    void getGifWhenCurrencyExistsAndRatioDecreased() {
        mockingOpenexchangeratesServiceClientDecreaseRates();
        String actual = gifService.getGifByCurrencyRateChange(quoteCurrency);
        assertEquals(actual, decreaseTag);
    }

    @Test
    void getGifWhenCurrencyExistsAndRatioHasNoChanges() {
        mockingOpenexchangeratesServiceClientNoChangeRates();
        String actual = gifService.getGifByCurrencyRateChange(quoteCurrency);
        assertEquals(actual, noChangeTag);
    }

    @Test
    void getGifWhenCurrencyExistsAndRatioNotAvailable() {
        mockingOpenexchangeratesServiceClientNotAvailableRates();
        assertThrows(FeignClientException.class,
            () -> gifService.getGifByCurrencyRateChange(quoteCurrency));
    }

    @Test
    void getGifWhenCurrencyDoesNotExist() {
        mockingOpenexchangeratesServiceClientNoRates();
        assertThrows(ResourceNotFoundException.class,
            () -> gifService.getGifByCurrencyRateChange(notExistingCurrency));
    }
    
    @Test
    void getGifWhenURLIsNotPresent() {

    }

}