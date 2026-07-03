package com.investmenttracker.service;

import com.investmenttracker.dto.CreatePositionRequest;
import com.investmenttracker.dto.PositionDto;
import com.investmenttracker.dto.PositionFilter;
import com.investmenttracker.dto.UpdatePostionRequest;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Position;
import com.investmenttracker.exception.PositionAccessDeniedException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.repository.PositionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {
    @Mock
    private PositionRepository positionRepository;
    @InjectMocks
    private PositionService positionService;
    @Test
    void shouldThrowAccessDeniedWhenPositionBelongsToAnotherUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = new Position().builder()
                .id(1L)
                .userId(2L)
                .build();
        when(positionRepository.findById(1L))
                .thenReturn(Optional.of(position));
        Assertions.assertThrows(PositionAccessDeniedException.class,()->positionService.getPositionById(1L));

    }
    @Test
    void createPosition_shouldCreatePositionForCurrentUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        CreatePositionRequest request = new CreatePositionRequest(
                "TEST",
                BigDecimal.TEN,
                Currency.EUR,
                BigDecimal.TEN
        );
        when(positionRepository.save(any(Position.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        positionService.createPosition(request);
        ArgumentCaptor<Position> captor = ArgumentCaptor.forClass(Position.class);
        verify(positionRepository).save(captor.capture());
        Position savedPosition = captor.getValue();
        Assertions.assertEquals("TEST", savedPosition.getTicker());
        Assertions.assertEquals(BigDecimal.TEN, savedPosition.getAveragePrice());
        Assertions.assertEquals(Currency.EUR, savedPosition.getCurrency());
        Assertions.assertEquals(BigDecimal.TEN, savedPosition.getQuantity());
    }
    @Test
    void getPositionById_shouldReturnPositionWhenOwner() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position =  Position.builder()
                .id(1L)
                .userId(1L)
                .ticker("NVDA")
                .currency(Currency.EUR)
                .averagePrice(BigDecimal.valueOf(10000))
                .quantity(BigDecimal.TEN)
                .build();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        PositionDto result = positionService.getPositionById(1L);
        Assertions.assertEquals(1L, result.id());
        Assertions.assertEquals("NVDA", result.ticker());
        Assertions.assertEquals(BigDecimal.valueOf(10000), result.averagePrice());
        Assertions.assertEquals(BigDecimal.TEN, result.quantity());
        Assertions.assertEquals(Currency.EUR, result.currency());
        Assertions.assertEquals(1L, result.userId());

    }
    @Test
    void getPositionById_shuldThrowNotFoundExceptionWhenPositionDoesNotExist() {
        when(positionRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(PositionNotFoundException.class, ()-> positionService.getPositionById(1L));
    }
    @Test
    void updatePosition_shouldUpdateOnlyProvidedFields() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = Position.builder()
                .id(1L)
                .userId(1L)
                .ticker("NVDA")
                .currency(Currency.EUR)
                .averagePrice(BigDecimal.TEN)
                .quantity(BigDecimal.TEN)
                .build();
        UpdatePostionRequest request = new UpdatePostionRequest("TSMC", null, null, null);
        when(positionRepository.findById(1L))
                .thenReturn(Optional.of(position));
        when(positionRepository.save(any(Position.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
        positionService.updatePosition(1L, request);
        ArgumentCaptor<Position> captor = ArgumentCaptor.forClass(Position.class);
        verify(positionRepository).save(captor.capture());
        Position savedPosition = captor.getValue();
        Assertions.assertEquals("TSMC", savedPosition.getTicker());
        Assertions.assertEquals(BigDecimal.TEN, savedPosition.getAveragePrice());
        Assertions.assertEquals(BigDecimal.TEN, savedPosition.getQuantity());
        Assertions.assertEquals(Currency.EUR, savedPosition.getCurrency());
    }
    @Test
    void updatePosition_shouldThrowAccessDeniedWhenPositionBelongsToAnotherUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = Position.builder()
                .id(1L)
                .userId(2L)
                .ticker("NVDA")
                .currency(Currency.EUR)
                .averagePrice(BigDecimal.TEN)
                .quantity(BigDecimal.TEN)
                .build();
        UpdatePostionRequest request = new UpdatePostionRequest("TSMC", null, null, null);
        when(positionRepository.findById(1L))
                .thenReturn(Optional.of(position));
        Assertions.assertThrows(PositionAccessDeniedException.class,()->positionService.updatePosition(1L, request));
        verify(positionRepository, never()).save(any(Position.class));
    }
    @Test
    void deletePosition_shouldDeletePositionWhenOwner() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = Position.builder()
                .id(1L)
                .userId(1L)
                .build();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        positionService.deletePositionById(position.getId());
        verify(positionRepository).delete(position);
    }
    @Test
    void deletePosition_shouldThrowAccessDeniedWhenPositionBelongsToAnotherUser() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = Position.builder()
                .id(1L)
                .userId(2L)
                .build();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(position));
        Assertions.assertThrows(PositionAccessDeniedException.class,()->positionService.deletePositionById(position.getId()));
        verify(positionRepository, never()).delete(any(Position.class));
    }
    @Test
    void getAllPositions_shouldReturnOnlyCurrentUserPositions() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(1L, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Position position = Position.builder()
                .id(1L)
                .userId(1L)
                .ticker("TEST")
                .currency(Currency.EUR)
                .averagePrice(BigDecimal.TEN)
                .quantity(BigDecimal.TEN)
                .build();
        PositionFilter filter = new PositionFilter("TEST", null, null, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        when(positionRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(position)));
        Page<PositionDto> result = positionService.getAllPositions(filter, pageable);
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals("TEST", result.getContent().get(0).ticker());
        Assertions.assertEquals(1L, result.getContent().get(0).userId());
        verify(positionRepository).findAll(any(Specification.class), any(Pageable.class));
    }
}
