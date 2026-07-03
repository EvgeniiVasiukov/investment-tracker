package com.investmenttracker.controller;

import com.investmenttracker.config.SecurityConfig;
import com.investmenttracker.dto.CreatePositionRequest;
import com.investmenttracker.dto.PositionDto;
import com.investmenttracker.dto.UpdatePostionRequest;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.exception.PositionAccessDeniedException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.security.JwtAuthenticationFilter;
import com.investmenttracker.security.JwtService;
import com.investmenttracker.service.PositionService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    @Test
    void createPosition_shouldReturn201WhenPositionIsCreated() throws Exception {
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
        when(jwtService.extractUserId("test-token")).thenReturn(1L);
        when(positionService.createPosition(any(CreatePositionRequest.class)))
                .thenReturn(dto);
        mockMvc.perform(post("/positions")
                .header("Authorization", "Bearer test-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "ticker": "NVDA",
                        "quantity": 10,
                        "currency": "EUR",
                        "averagePrice": 50
                        }"""))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker").value("NVDA"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.currency").value("EUR"));

    }
    @Test
    void deletePositionById_shouldReturn204WhenPositionIsFound() throws Exception {
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(1L);
        mockMvc.perform(delete("/positions/1")
                .header("Authorization", "Bearer test-token"))
                .andExpect(status().isNoContent());
                verify(positionService).deletePositionById(1L);
    }
    @Test
    void deletePositionById_shouldReturn403WhenPositionBelongsToAnotherUser() throws Exception {
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(2L);
        doThrow(new PositionAccessDeniedException("Access denied")).when(positionService).deletePositionById(1L);
        mockMvc.perform(delete("/positions/1")
        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isForbidden());
    }
    @Test
    void updatePosition_shouldReturn200WhenPositionIsUpdated() throws Exception {
        PositionDto dto = new PositionDto(1L,
                1L,
                "NVDA",
                BigDecimal.TEN,
                BigDecimal.TEN,
                Currency.EUR,
                LocalDateTime.now());
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(1L);
        when(positionService.updatePosition(eq(1L), any(UpdatePostionRequest.class)))
                .thenReturn(dto);
        mockMvc.perform(patch("/positions/1")
                .header("Authorization", "Bearer test-token")
        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "ticker": "UPD"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("NVDA"));
    }
    @Test
    void updatePosition_shouldReturn403WhenPositionBelongsToAnotherUser() throws Exception {
        when(jwtService.isTokenValid("test-token")).thenReturn(true);
        when(jwtService.extractUserId("test-token")).thenReturn(2L);
        when(positionService.updatePosition(eq(1L), any(UpdatePostionRequest.class)))
                .thenThrow(new PositionAccessDeniedException("Access denied"));
        mockMvc.perform(patch("/positions/1")
                .header("Authorization", "Bearer test-token")
        .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "ticker": "UPD"
                        }
                        """))
                .andExpect(status().isForbidden());
    }
}
