package com.investmenttracker;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import com.investmenttracker.client.AlphaVantageMarketClient;
import com.investmenttracker.config.AlphaVantageProperties;
import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.exception.MarketDataProviderException;
import com.investmenttracker.model.Currency;
import com.investmenttracker.model.MarketDataProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class AlphaVantageMarketClientTest {
    private WireMockServer wireMockServer;
    private AlphaVantageMarketClient client;

    @BeforeEach
    public void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());
        wireMockServer.start();
        AlphaVantageProperties properties = new AlphaVantageProperties();
        properties.setBaseUrl(wireMockServer.baseUrl());
        properties.setApiKey("test");
        RestClient restClient = RestClient.builder().baseUrl(properties.getBaseUrl()).build();
        client = new AlphaVantageMarketClient(properties, restClient);
    }
    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }


    @Test
    void shouldReturnPriceFromAlphaVantage() {
        givenAlphaVintageReturnsPrice();
        PriceDto result = client.getPrice("IBM");
        assertPriceDto(result);

    }
    @Test
    void shouldThrowMarketDataProviderExceptionWhenApiReturns500() {
        wireMockServer.stubFor(get(urlPathEqualTo("/query"))
                .willReturn(serverError()));
        Assertions.assertThrows(MarketDataProviderException.class, () -> client.getPrice("IBM"));
    }
    @Test
    void shouldThrowMarketDataProviderExceptionWhenResponseIsInvalid() {
        wireMockServer.stubFor(get(urlPathEqualTo("/query"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                "Global Quote": {}
                                }
                                """)));
        Assertions.assertThrows(MarketDataProviderException.class, () -> client.getPrice("IBM"));
    }
    @Test
    void shouldThrowMarketDataProviderExceptionWhenResponseIsNull() {
        wireMockServer.stubFor(get(urlPathEqualTo("/query"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                "Global Quote": null
                                }
                                """)));
        Assertions.assertThrows(MarketDataProviderException.class, () -> client.getPrice("IBM"));
    }

    private void givenAlphaVintageReturnsPrice() {
        wireMockServer.stubFor(get(urlPathEqualTo("/query"))
                .willReturn(ok()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "Global Quote": {
                                        "01. symbol": "IBM",
                                        "02. open": "279.6600",
                                        "03. high": "294.4900",
                                        "04. low": "278.9600",
                                        "05. price": "286.2500",
                                        "06. volume": "6905811",
                                        "07. latest trading day": "2026-07-01",
                                        "08. previous close": "281.2100",
                                        "09. change": "5.0400",
                                        "10. change percent": "1.7923%"
                                    }
                                }
                                """)));
    }
    private void assertPriceDto(PriceDto priceDto) {
        Assertions.assertEquals("IBM", priceDto.ticker());
        Assertions.assertEquals(0, new BigDecimal(286.2500).compareTo(priceDto.currentPrice()));
        Assertions.assertEquals(Currency.USD, priceDto.currency());
        Assertions.assertEquals(MarketDataProvider.ALPHA_VANTAGE, priceDto.provider());
    }
}
