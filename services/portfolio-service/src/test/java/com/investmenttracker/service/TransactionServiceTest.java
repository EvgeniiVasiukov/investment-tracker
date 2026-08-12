package com.investmenttracker.service;

import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.request.SellTransactionRequest;
import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.dto.response.SellTransactionResponse;
import com.investmenttracker.dto.response.TransactionResponse;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.entity.Position;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import com.investmenttracker.exception.InsufficientPositionQuantityException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.repository.TransactionRepository;
import com.investmenttracker.security.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateNewPositionWhenPositionDoesNotExist() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            BuyTransactionRequest request = new BuyTransactionRequest(
                    "TEST",
                    BigDecimal.TEN,
                    new BigDecimal("100.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.empty());

            when(positionRepository.save(any(Position.class)))
                    .thenAnswer(invocation -> {
                        Position position = invocation.getArgument(0);
                        position.setId(100L);
                        return position;
                    });

            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transaction.setId(200L);
                        return transaction;
                    });

            BuyTransactionResponse response =
                    transactionService.processBuy(request);

            // Response
            assertEquals(200L, response.transactionId());
            assertEquals(100L, response.positionId());
            assertEquals("TEST", response.ticker());
            assertEquals(BigDecimal.TEN, response.quantity());
            assertEquals(new BigDecimal("100.00"), response.averagePrice());
            assertEquals(Currency.USD, response.currency());

            // Position passed to repository
            ArgumentCaptor<Position> positionCaptor =
                    ArgumentCaptor.forClass(Position.class);

            verify(positionRepository)
                    .save(positionCaptor.capture());

            Position position = positionCaptor.getValue();

            assertEquals("TEST", position.getTicker());
            assertEquals(1L, position.getUserId());
            assertEquals(BigDecimal.TEN, position.getQuantity());
            assertEquals(new BigDecimal("100.00"), position.getAveragePrice());
            assertEquals(Currency.USD, position.getCurrency());

            // Transaction passed to repository
            ArgumentCaptor<Transaction> transactionCaptor =
                    ArgumentCaptor.forClass(Transaction.class);

            verify(transactionRepository)
                    .save(transactionCaptor.capture());

            Transaction transaction = transactionCaptor.getValue();

            assertEquals("TEST", transaction.getTicker());
            assertEquals(1L, transaction.getUserId());
            assertEquals(BigDecimal.TEN, transaction.getQuantity());
            assertEquals(new BigDecimal("100.00"), transaction.getPrice());
            assertEquals(Currency.USD, transaction.getCurrency());
            assertEquals(BigDecimal.ONE, transaction.getFees());
            assertEquals(BigDecimal.ZERO, transaction.getTax());
            assertEquals(TransactionType.BUY, transaction.getTransactionType());
            assertEquals(request.transactionDate(), transaction.getTransactionDate());

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);
        }
    }
    @Test
    void shouldUpdateExistingPositionWhenBuyingExistingTicker() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Position existingPosition = Position.builder()
                    .id(100L)
                    .userId(1L)
                    .ticker("TEST")
                    .quantity(BigDecimal.TEN)
                    .averagePrice(new BigDecimal("100.00"))
                    .currency(Currency.USD)
                    .createdAt(LocalDateTime.now())
                    .build();

            BuyTransactionRequest request = new BuyTransactionRequest(
                    "TEST",
                    BigDecimal.TEN,
                    new BigDecimal("200.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.of(existingPosition));

            when(positionRepository.save(any(Position.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transaction.setId(200L);
                        return transaction;
                    });

            BuyTransactionResponse response =
                    transactionService.processBuy(request);

            // Response
            assertEquals(200L, response.transactionId());
            assertEquals(100L, response.positionId());
            assertEquals("TEST", response.ticker());
            assertEquals(new BigDecimal("20"), response.quantity());
            assertEquals(new BigDecimal("150.000000"), response.averagePrice());
            assertEquals(Currency.USD, response.currency());

            // Updated Position
            ArgumentCaptor<Position> positionCaptor =
                    ArgumentCaptor.forClass(Position.class);

            verify(positionRepository)
                    .save(positionCaptor.capture());

            Position updatedPosition = positionCaptor.getValue();

            assertEquals(100L, updatedPosition.getId());
            assertEquals(1L, updatedPosition.getUserId());
            assertEquals("TEST", updatedPosition.getTicker());
            assertEquals(new BigDecimal("20"), updatedPosition.getQuantity());
            assertEquals(
                    new BigDecimal("150.000000"),
                    updatedPosition.getAveragePrice()
            );
            assertEquals(Currency.USD, updatedPosition.getCurrency());

            // BUY Transaction
            ArgumentCaptor<Transaction> transactionCaptor =
                    ArgumentCaptor.forClass(Transaction.class);

            verify(transactionRepository)
                    .save(transactionCaptor.capture());

            Transaction transaction = transactionCaptor.getValue();

            assertEquals(200L, transaction.getId());
            assertEquals(1L, transaction.getUserId());
            assertEquals("TEST", transaction.getTicker());

            // Transaction contains only this purchase,
            // not the resulting total Position quantity.
            assertEquals(BigDecimal.TEN, transaction.getQuantity());
            assertEquals(new BigDecimal("200.00"), transaction.getPrice());

            assertEquals(Currency.USD, transaction.getCurrency());
            assertEquals(BigDecimal.ONE, transaction.getFees());
            assertEquals(BigDecimal.ZERO, transaction.getTax());
            assertEquals(TransactionType.BUY, transaction.getTransactionType());
            assertEquals(
                    request.transactionDate(),
                    transaction.getTransactionDate()
            );

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);
        }
    }
    @Test
    void shouldDecreasePositionWhenSellingPartially() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Position existingPosition = Position.builder()
                    .id(100L)
                    .userId(1L)
                    .ticker("TEST")
                    .quantity(BigDecimal.TEN)
                    .averagePrice(new BigDecimal("100.00"))
                    .currency(Currency.USD)
                    .createdAt(LocalDateTime.now())
                    .build();

            SellTransactionRequest request = new SellTransactionRequest(
                    "TEST",
                    new BigDecimal("4"),
                    new BigDecimal("150.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.of(existingPosition));

            when(positionRepository.save(any(Position.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transaction.setId(200L);
                        return transaction;
                    });

            SellTransactionResponse response =
                    transactionService.processSell(request);

            // Response
            assertEquals(200L, response.transactionId());
            assertEquals(100L, response.positionId());
            assertEquals("TEST", response.ticker());
            assertEquals(new BigDecimal("6"), response.quantity());
            assertEquals(new BigDecimal("100.00"), response.averagePrice());
            assertEquals(Currency.USD, response.currency());

            // Updated Position
            ArgumentCaptor<Position> positionCaptor =
                    ArgumentCaptor.forClass(Position.class);

            verify(positionRepository)
                    .save(positionCaptor.capture());

            Position updatedPosition = positionCaptor.getValue();

            assertEquals(100L, updatedPosition.getId());
            assertEquals(1L, updatedPosition.getUserId());
            assertEquals("TEST", updatedPosition.getTicker());
            assertEquals(new BigDecimal("6"), updatedPosition.getQuantity());

            // SELL must not change average purchase price
            assertEquals(
                    new BigDecimal("100.00"),
                    updatedPosition.getAveragePrice()
            );

            assertEquals(Currency.USD, updatedPosition.getCurrency());

            // SELL Transaction
            ArgumentCaptor<Transaction> transactionCaptor =
                    ArgumentCaptor.forClass(Transaction.class);

            verify(transactionRepository)
                    .save(transactionCaptor.capture());

            Transaction transaction = transactionCaptor.getValue();

            assertEquals(200L, transaction.getId());
            assertEquals(1L, transaction.getUserId());
            assertEquals("TEST", transaction.getTicker());
            assertEquals(new BigDecimal("4"), transaction.getQuantity());
            assertEquals(new BigDecimal("150.00"), transaction.getPrice());
            assertEquals(Currency.USD, transaction.getCurrency());
            assertEquals(BigDecimal.ONE, transaction.getFees());
            assertEquals(BigDecimal.ZERO, transaction.getTax());
            assertEquals(TransactionType.SELL, transaction.getTransactionType());
            assertEquals(request.transactionDate(), transaction.getTransactionDate()
            );

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);
        }
    }
    @Test
    void shouldDeletePositionWhenSellingEntirePosition() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Position existingPosition = Position.builder()
                    .id(100L)
                    .userId(1L)
                    .ticker("TEST")
                    .quantity(BigDecimal.TEN)
                    .averagePrice(new BigDecimal("100.00"))
                    .currency(Currency.USD)
                    .createdAt(LocalDateTime.now())
                    .build();

            SellTransactionRequest request = new SellTransactionRequest(
                    "TEST",
                    BigDecimal.TEN,
                    new BigDecimal("150.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.of(existingPosition));

            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transaction.setId(200L);
                        return transaction;
                    });

            SellTransactionResponse response =
                    transactionService.processSell(request);

            // Response
            assertEquals(200L, response.transactionId());
            assertEquals(100L, response.positionId());
            assertEquals("TEST", response.ticker());
            assertEquals(BigDecimal.ZERO, response.quantity());
            assertEquals(new BigDecimal("100.00"), response.averagePrice());
            assertEquals(Currency.USD, response.currency());

            // Position must be deleted
            verify(positionRepository)
                    .delete(existingPosition);

            // No update/save should happen after full sale
            verify(positionRepository, never())
                    .save(any(Position.class));

            // SELL Transaction
            ArgumentCaptor<Transaction> transactionCaptor =
                    ArgumentCaptor.forClass(Transaction.class);

            verify(transactionRepository)
                    .save(transactionCaptor.capture());

            Transaction transaction = transactionCaptor.getValue();

            assertEquals(200L, transaction.getId());
            assertEquals(1L, transaction.getUserId());
            assertEquals("TEST", transaction.getTicker());
            assertEquals(BigDecimal.TEN, transaction.getQuantity());
            assertEquals(new BigDecimal("150.00"), transaction.getPrice());
            assertEquals(Currency.USD, transaction.getCurrency());
            assertEquals(BigDecimal.ONE, transaction.getFees());
            assertEquals(BigDecimal.ZERO, transaction.getTax());
            assertEquals(TransactionType.SELL, transaction.getTransactionType());
            assertEquals(
                    request.transactionDate(),
                    transaction.getTransactionDate()
            );

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);
        }
    }
    @Test
    void shouldThrowPositionNotFoundWhenSellingNonExistingTicker() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            SellTransactionRequest request = new SellTransactionRequest(
                    "TEST",
                    BigDecimal.ONE,
                    new BigDecimal("150.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.empty());

            assertThrows(
                    PositionNotFoundException.class,
                    () -> transactionService.processSell(request)
            );

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);

            verify(positionRepository, never())
                    .save(any(Position.class));

            verify(positionRepository, never())
                    .delete(any(Position.class));

            verify(transactionRepository, never())
                    .save(any(Transaction.class));
        }
    }
    @Test
    void shouldThrowInsufficientPositionQuantityWhenSellingTooMuch() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Position existingPosition = Position.builder()
                    .id(100L)
                    .userId(1L)
                    .ticker("TEST")
                    .quantity(BigDecimal.TEN)
                    .averagePrice(new BigDecimal("100.00"))
                    .currency(Currency.USD)
                    .createdAt(LocalDateTime.now())
                    .build();

            SellTransactionRequest request = new SellTransactionRequest(
                    "TEST",
                    new BigDecimal("15"),
                    new BigDecimal("150.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.of(existingPosition));

            assertThrows(
                    InsufficientPositionQuantityException.class,
                    () -> transactionService.processSell(request)
            );

            verify(positionRepository)
                    .findByTickerAndUserId("TEST", 1L);

            verify(positionRepository, never())
                    .save(any(Position.class));

            verify(positionRepository, never())
                    .delete(any(Position.class));

            verify(transactionRepository, never())
                    .save(any(Transaction.class));
        }
    }
    @Test
    void shouldCalculateRealizedLossWhenSellingBelowAveragePrice() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Position existingPosition = Position.builder()
                    .id(100L)
                    .userId(1L)
                    .ticker("TEST")
                    .quantity(BigDecimal.TEN)
                    .averagePrice(new BigDecimal("100.00"))
                    .currency(Currency.USD)
                    .createdAt(LocalDateTime.now())
                    .build();

            SellTransactionRequest request = new SellTransactionRequest(
                    "TEST",
                    new BigDecimal("4"),
                    new BigDecimal("80.00"),
                    Currency.USD,
                    BigDecimal.ONE,
                    BigDecimal.ZERO,
                    LocalDateTime.of(2026, 8, 10, 12, 0)
            );

            when(positionRepository.findByTickerAndUserId("TEST", 1L))
                    .thenReturn(Optional.of(existingPosition));

            when(positionRepository.save(any(Position.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(transactionRepository.save(any(Transaction.class)))
                    .thenAnswer(invocation -> {
                        Transaction transaction = invocation.getArgument(0);
                        transaction.setId(200L);
                        return transaction;
                    });

            SellTransactionResponse response =
                    transactionService.processSell(request);

            assertEquals(
                    new BigDecimal("-80.00"),
                    response.realizedProfitLoss()
            );

            ArgumentCaptor<Transaction> transactionCaptor =
                    ArgumentCaptor.forClass(Transaction.class);

            verify(transactionRepository)
                    .save(transactionCaptor.capture());

            Transaction transaction = transactionCaptor.getValue();

            assertEquals(
                    new BigDecimal("-80.00"),
                    transaction.getRealizedProfitLoss()
            );
        }
    }
        @Test
        void shouldCalculateZeroRealizedProfitLossWhenSellingAtAveragePrice() {

            try (MockedStatic<SecurityUtils> securityUtilsMock =
                         Mockito.mockStatic(SecurityUtils.class)) {

                securityUtilsMock
                        .when(SecurityUtils::getCurrentUserId)
                        .thenReturn(1L);

                Position existingPosition = Position.builder()
                        .id(100L)
                        .userId(1L)
                        .ticker("TEST")
                        .quantity(BigDecimal.TEN)
                        .averagePrice(new BigDecimal("100.00"))
                        .currency(Currency.USD)
                        .createdAt(LocalDateTime.now())
                        .build();

                SellTransactionRequest request = new SellTransactionRequest(
                        "TEST",
                        new BigDecimal("4"),
                        new BigDecimal("100.00"),
                        Currency.USD,
                        BigDecimal.ONE,
                        BigDecimal.ZERO,
                        LocalDateTime.of(2026, 8, 10, 12, 0)
                );

                when(positionRepository.findByTickerAndUserId("TEST", 1L))
                        .thenReturn(Optional.of(existingPosition));

                when(positionRepository.save(any(Position.class)))
                        .thenAnswer(invocation -> invocation.getArgument(0));

                when(transactionRepository.save(any(Transaction.class)))
                        .thenAnswer(invocation -> {
                            Transaction transaction = invocation.getArgument(0);
                            transaction.setId(200L);
                            return transaction;
                        });

                SellTransactionResponse response =
                        transactionService.processSell(request);

                assertEquals(
                        0,
                        BigDecimal.ZERO.compareTo(response.realizedProfitLoss())
                );

                ArgumentCaptor<Transaction> transactionCaptor =
                        ArgumentCaptor.forClass(Transaction.class);

                verify(transactionRepository)
                        .save(transactionCaptor.capture());

                Transaction transaction = transactionCaptor.getValue();

                assertEquals(
                        0,
                        BigDecimal.ZERO.compareTo(transaction.getRealizedProfitLoss())
                );
            }
        }
    @Test
    void shouldReturnTransactionHistoryForCurrentUser() {

        try (MockedStatic<SecurityUtils> securityUtilsMock =
                     Mockito.mockStatic(SecurityUtils.class)) {

            securityUtilsMock
                    .when(SecurityUtils::getCurrentUserId)
                    .thenReturn(1L);

            Transaction transaction = Transaction.builder()
                    .id(100L)
                    .userId(1L)
                    .transactionType(TransactionType.SELL)
                    .ticker("NVDA")
                    .quantity(new BigDecimal("2.000000"))
                    .price(new BigDecimal("150.000000"))
                    .currency(Currency.USD)
                    .fees(BigDecimal.ONE)
                    .tax(BigDecimal.ZERO)
                    .realizedProfitLoss(new BigDecimal("100.000000"))
                    .transactionDate(LocalDateTime.of(2026, 8, 10, 12, 0))
                    .build();

            TransactionFilter filter = new TransactionFilter(
                    "NVDA",
                    TransactionType.SELL,
                    null,
                    null
            );

            Pageable pageable = PageRequest.of(0, 20);

            Page<Transaction> transactionPage =
                    new PageImpl<>(List.of(transaction), pageable, 1);

            when(transactionRepository.findAll(
                    any(Specification.class),
                    eq(pageable)
            )).thenReturn(transactionPage);

            Page<TransactionResponse> result =
                    transactionService.getAllTransactions(filter, pageable);

            assertEquals(1, result.getTotalElements());

            TransactionResponse response = result.getContent().getFirst();

            assertEquals(100L, response.id());
            assertEquals(TransactionType.SELL, response.transactionType());
            assertEquals("NVDA", response.ticker());
            assertEquals(new BigDecimal("2.000000"), response.quantity());
            assertEquals(new BigDecimal("150.000000"), response.price());
            assertEquals(Currency.USD, response.currency());
            assertEquals(new BigDecimal("100.000000"), response.realizedProfitLoss());

            verify(transactionRepository)
                    .findAll(any(Specification.class), eq(pageable));
        }
    }


    }


