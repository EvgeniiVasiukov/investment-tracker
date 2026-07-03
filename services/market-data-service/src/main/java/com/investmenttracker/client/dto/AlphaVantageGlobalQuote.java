package com.investmenttracker.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AlphaVantageGlobalQuote (
        @JsonProperty("01. symbol")
        String symbol,
        @JsonProperty("02. open")
        BigDecimal startingPrice,
        @JsonProperty("03. high")
        BigDecimal highestDailyPrice,
        @JsonProperty("04. low")
        BigDecimal lowestDailyPrice,
        @JsonProperty("05. price")
        BigDecimal currentPrice,
        @JsonProperty("06. volume")
        BigDecimal volume,
        @JsonProperty("07. latest trading day")
        LocalDate latestTradingDay,
        @JsonProperty("08. previous close")
        BigDecimal previousClosingPrice,
        @JsonProperty("09. change")
        BigDecimal absoluteDailyPriceChange,
        @JsonProperty("10 change percent")
        String relativeDailyPriceChange
){
}
