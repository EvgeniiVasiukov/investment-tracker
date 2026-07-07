package com.investmenttracker.client.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AlphaVantageGlobalQuoteResponse(
        @JsonProperty("Global Quote")
        AlphaVantageGlobalQuote globalQuote,
        @JsonProperty("Information")
        String information

) {
}
