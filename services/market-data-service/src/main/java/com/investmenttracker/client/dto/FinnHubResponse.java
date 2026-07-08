package com.investmenttracker.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinnHubResponse(
        @JsonProperty("c")
        BigDecimal currentPrice,
        @JsonProperty("d")
        BigDecimal change,
        @JsonProperty("dp")
        BigDecimal relativeDailyPriceChange,
        @JsonProperty("h")
        BigDecimal highestDailyPrice,
        @JsonProperty("l")
        BigDecimal lowestDailyPrice,
        @JsonProperty("o")
        BigDecimal openingPrice,
        @JsonProperty("pc")
        BigDecimal previousClosingPrice,
        @JsonProperty("t")
        Long timeStamp

) {
}
