package com.investmenttracker.controller;

import com.investmenttracker.config.SecurityConfig;
import com.investmenttracker.dto.request.CreatePositionRequest;
import com.investmenttracker.dto.response.PositionDto;
import com.investmenttracker.dto.request.UpdatePostionRequest;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.exception.PositionAccessDeniedException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.security.JwtAuthenticationFilter;
import com.investmenttracker.security.JwtService;
import com.investmenttracker.service.PositionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PositionController.class)
@Import({SecurityConfig.class,
JwtAuthenticationFilter.class})
public class PositionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private PositionService positionService;
    @MockitoBean
    private JwtService jwtService;

    @Test
    void getPositionById_shouldReturn200WhenPositionIsFound() throws Exception {
        PositionDto dto = new PositionDto(
                1L,
                1L,
                "NVDA",
                BigDecimal.TEN,
                BigDecimal.TEN,
                Currency.EUR,
                LocalDateTime.now()
        );
        when(jwtService.isTokenValid("test-token"))
                .thenReturn(true);
        when(jwtService.extractUserId("test-token"))
                .thenReturn(1L);
        when(positionService.getPositionById(1L)).thenReturn(dto);
        mockMvc.perform(get("/positions/1")
                        .header("Authorization", "Bearer test-token"))
        .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("NVDA"))
                .andExpect(jsonPath("$.userId").value(1L));
    }
    @Test
    void getPositionById_shouldReturn404WhenPositionIsNotFound() throws Exception {
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(1L);
        when(positionService.getPositionById(1L)).thenThrow(new PositionNotFoundException("Position not found"));
        mockMvc.perform(get("/positions/1").header("Authorization", "Bearer test-token"))
                .andExpect(status().isNotFound());
    }
    @Test
    void getPositionById_shouldReturn403WhenPositionBelongsToAnotherUser() throws Exception {
        PositionDto dto = new PositionDto(
                1L,
                1L,
                "NVDA",
                BigDecimal.TEN,
                BigDecimal.TEN,
                Currency.EUR,
                LocalDateTime.now()
        );
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(2L);
        when(positionService.getPositionById(1L)).thenThrow(new PositionAccessDeniedException("Position not found"));
        mockMvc.perform(get("/positions/1").header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }
}
