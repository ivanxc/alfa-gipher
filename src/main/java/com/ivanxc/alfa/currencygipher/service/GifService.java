package com.ivanxc.alfa.currencygipher.service;

import com.ivanxc.alfa.currencygipher.client.OpenexchangeratesServiceClient;
import com.ivanxc.alfa.currencygipher.client.GiphyServiceClient;
import com.ivanxc.alfa.currencygipher.dto.GifDto;
import com.ivanxc.alfa.currencygipher.exception.GiphyImageObjectException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifService {

    private final OpenexchangeratesServiceClient openexchangeratesServiceClient;
    private final GiphyServiceClient giphyServiceClient;
    @Value("${application.openexchangerates.service.app-id}")
    private final String openexchangeratesAppId;
    @Value("${application.giphy.service.app-id}")
    private final String giphyAppId;
    @Value("${application.openexchangerates.service.base-currency}")
    private final String baseCurrency;
    @Value("${application.giphy.service.rendition}")
    private final String rendition;
    @Value("${application.giphy.service.tag.increase}")
    private final String increaseTag;
    @Value("${application.giphy.service.tag.decrease}")
    private final String decreaseTag;
    @Value("${application.giphy.service.tag.no-change}")
    private final String noChangeTag;

    private enum CurrencyRateChange {
        INCREASED,
        DECREASED,
        NO_CHANGE
    }

    public String getGifByCurrencyRateChange(String quoteCurrency) {
        CurrencyRateChange compareRatesResult = getChange(quoteCurrency);
        String tag = getTagByCurrencyRateChange(compareRatesResult);
        GifDto gifDto = giphyServiceClient.getRandomGifByWord(tag, giphyAppId);
        return getURLFromGitDto(gifDto);
    }

    private String getURLFromGitDto(GifDto gifDto) {
        String gifURL = Optional.ofNullable(
            gifDto.getData()
                .getImageDto(rendition)
                .orElseThrow(() -> new GiphyImageObjectException("Not supported rendition" + rendition))
                .getUrl()
            )
            .orElseThrow(
                () -> new GiphyImageObjectException(
                    "URL is not present for rendition " + rendition)
            );

        gifURL = gifURL.replaceFirst("[0-9]*media[0-9]*", "i");
        return gifURL;
    }

    private String getTagByCurrencyRateChange(CurrencyRateChange change) {
        String tag = "";
        if (change == CurrencyRateChange.INCREASED) {
            tag = increaseTag;
        } else if (change == CurrencyRateChange.DECREASED) {
            tag = decreaseTag;
        } else {
            tag = noChangeTag;
        }
        return tag;
    }

    private CurrencyRateChange getChange(String quoteCurrency) {
        String todayDate = LocalDate.now().toString();
        String yesterdayDate = LocalDate.now().minusDays(1).toString();

        double todayBaseRate = openexchangeratesServiceClient
            .findByDateAndSymbol(todayDate, baseCurrency, openexchangeratesAppId)
            .getRatio(baseCurrency);
        double yesterdayBaseRate = openexchangeratesServiceClient
            .findByDateAndSymbol(yesterdayDate, baseCurrency, openexchangeratesAppId)
            .getRatio(baseCurrency);

        double todayQuoteRate = openexchangeratesServiceClient
            .findByDateAndSymbol(todayDate, quoteCurrency, openexchangeratesAppId)
            .getRatio(quoteCurrency);
        double yesterdayQuoteRate = openexchangeratesServiceClient
            .findByDateAndSymbol(yesterdayDate, quoteCurrency, openexchangeratesAppId)
            .getRatio(quoteCurrency);

        BigDecimal todayBase = BigDecimal.valueOf(todayBaseRate);
        BigDecimal yesterdayBase = BigDecimal.valueOf(yesterdayBaseRate);
        BigDecimal todayQuote = BigDecimal.valueOf(todayQuoteRate);
        BigDecimal yesterdayQuote = BigDecimal.valueOf(yesterdayQuoteRate);

        BigDecimal todayPairRate = todayQuote.divide(todayBase, 6, RoundingMode.FLOOR);
        BigDecimal yesterdayPairRate = yesterdayQuote.divide(yesterdayBase, 6, RoundingMode.FLOOR);

        int compareRatesResult = todayPairRate.compareTo(yesterdayPairRate);

        if (compareRatesResult > 0) {
            return CurrencyRateChange.INCREASED;
        } else if (compareRatesResult < 0) {
            return CurrencyRateChange.DECREASED;
        } else {
            return CurrencyRateChange.NO_CHANGE;
        }
    }
}