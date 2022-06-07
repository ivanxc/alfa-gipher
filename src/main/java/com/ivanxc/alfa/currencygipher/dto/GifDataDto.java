package com.ivanxc.alfa.currencygipher.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.Optional;
import lombok.Data;

@Data
public class GifDataDto {
    @JsonProperty("embed_url")
    private String embedUrl;
    private Map<String, GifImageDto> images;

    public Optional<GifImageDto> getImageDto(String objectName) {
        return Optional.ofNullable(images.get(objectName));
    }
}
