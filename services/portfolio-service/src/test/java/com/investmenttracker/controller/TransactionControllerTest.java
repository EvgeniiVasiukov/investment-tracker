package com.investmenttracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.investmenttracker.config.SecurityConfig;
import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.request.SellTransactionRequest;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.dto.response.SellTransactionResponse;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.exception.InsufficientPositionQuantityException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.security.JwtAuthenticationFilter;
import com.investmenttracker.security.JwtService;
import com.investmenttracker.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class})
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser
    void shouldCreateBuyTransaction() throws Exception {

        BuyTransactionRequest request = new BuyTransactionRequest(
                "TEST",
                BigDecimal.TEN,
                new BigDecimal("100.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        BuyTransactionResponse response = new BuyTransactionResponse(
                200L,
                100L,
                "TEST",
                BigDecimal.TEN,
                new BigDecimal("100.00"),
                Currency.USD
        );

        when(transactionService.processBuy(any(BuyTransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON));
                mockMvc.perform(post("/transactions/buy")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                              "ticker": "TEST",
                              "quantity": 10,
                              "price": 100.00,
                              "currency": "USD",
                              "fees": 1.00,
                              "tax": 0,
                              "transactionDate": "2026-08-10T12:00:00"
                            }
                            """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(200L))
                .andExpect(jsonPath("$.positionId").value(100L))
                .andExpect(jsonPath("$.ticker").value("TEST"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.averagePrice").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    @WithMockUser
    void shouldCreateSellTransaction() throws Exception {

        SellTransactionRequest request = new SellTransactionRequest(
                "TEST",
                new BigDecimal("4"),
                new BigDecimal("150.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        SellTransactionResponse response = new SellTransactionResponse(
                200L,
                100L,
                "TEST",
                new BigDecimal("6"),
                new BigDecimal("100.00"),
                Currency.USD,
                new BigDecimal("200.00")
        );

        when(transactionService.processSell(any(SellTransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
        {
          "ticker": "TEST",
          "quantity": 4,
          "price": 150.00,
          "currency": "USD",
          "fees": 1.00,
          "tax": 0,
          "transactionDate": "2026-08-10T12:00:00",
          "realizedProfitLoss": 200.00
        }
        """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(200L))
                .andExpect(jsonPath("$.positionId").value(100L))
                .andExpect(jsonPath("$.ticker").value("TEST"))
                .andExpect(jsonPath("$.quantity").value(6))
                .andExpect(jsonPath("$.averagePrice").value(100.00))
                .andExpect(jsonPath("$.currency").value("USD"))
                .andExpect(jsonPath("$.realizedProfitLoss").value(200.00));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestForInvalidBuyRequest() throws Exception {

        BuyTransactionRequest request = new BuyTransactionRequest(
                "",
                BigDecimal.ZERO,
                new BigDecimal("100.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        mockMvc.perform(post("/transactions/buy")
                        .contentType(MediaType.APPLICATION_JSON)
                .content("""
        {
          "ticker": "",
          "quantity": 0,
          "price": 100.00,
          "currency": "USD",
          "fees": 1.00,
          "tax": 0,
          "transactionDate": "2026-08-10T12:00:00"
        }
        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestForInvalidSellRequest() throws Exception {

        SellTransactionRequest request = new SellTransactionRequest(
                "",
                BigDecimal.ZERO,
                new BigDecimal("150.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        mockMvc.perform(post("/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
          "ticker": "",
          "quantity": 0,
          "price": 150.00,
          "currency": "USD",
          "fees": 1.00,
          "tax": 0,
          "transactionDate": "2026-08-10T12:00:00"
        }
        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void shouldReturnNotFoundWhenPositionDoesNotExist() throws Exception {

        SellTransactionRequest request = new SellTransactionRequest(
                "TEST",
                BigDecimal.ONE,
                new BigDecimal("150.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        when(transactionService.processSell(any(SellTransactionRequest.class)))
                .thenThrow(
                        new PositionNotFoundException(
                                "Position with ticker TEST was not found"
                        )
                );

        mockMvc.perform(post("/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
          "ticker": "TEST",
          "quantity": 1,
          "price": 150.00,
          "currency": "USD",
          "fees": 1.00,
          "tax": 0,
          "transactionDate": "2026-08-10T12:00:00"
        }
        """))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void shouldReturnTeapotWhenPositionQuantityIsInsufficient() throws Exception {

        SellTransactionRequest request = new SellTransactionRequest(
                "TEST",
                new BigDecimal("15"),
                new BigDecimal("150.00"),
                Currency.USD,
                BigDecimal.ONE,
                BigDecimal.ZERO,
                LocalDateTime.of(2026, 8, 10, 12, 0)
        );

        when(transactionService.processSell(any(SellTransactionRequest.class)))
                .thenThrow(
                        new InsufficientPositionQuantityException(
                                "Not enough stocks of TEST to sell"
                        )
                );

        mockMvc.perform(post("/transactions/sell")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
        {
          "ticker": "TEST",
          "quantity": 15,
          "price": 150.00,
          "currency": "USD",
          "fees": 1.00,
          "tax": 0,
          "transactionDate": "2026-08-10T12:00:00"
        }
        """))
                .andExpect(status().isIAmATeapot());
    }
}