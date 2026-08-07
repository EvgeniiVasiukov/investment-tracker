package com.investmenttracker.service;

import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.entity.Position;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.repository.TransactionRepository;
import com.investmenttracker.security.SecurityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PositionRepository positionRepository;

    public TransactionService(TransactionRepository transactionRepository, PositionRepository positionRepository) {
        this.transactionRepository = transactionRepository;
        this.positionRepository = positionRepository;
    }
    @Transactional
    public BuyTransactionResponse processBuy(BuyTransactionRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String ticker = request.ticker();
        Optional<Position> byTickerAndUserId = positionRepository.findByTickerAndUserId(ticker, currentUserId);
        Position result;
        if (byTickerAndUserId.isEmpty()) {
            result = createNewPosition(request, ticker, currentUserId);
        } else {
            result = updateExistingPosition(request, byTickerAndUserId.get());
        }
        Transaction savedTransaction = createNewTransaction(request, currentUserId);
        return toBuyTransactionResponse(result, savedTransaction);
    }

    private Position createNewPosition(BuyTransactionRequest request, String ticker, Long userId) {
        Position newPosition = Position.builder()
                .ticker(ticker)
                .userId(userId)
                .quantity(request.quantity())
                .averagePrice(request.price())
                .currency(request.currency())
                .createdAt(LocalDateTime.now())
                .build();
       return positionRepository.save(newPosition);
    }
    private Transaction createNewTransaction(BuyTransactionRequest request, Long userId) {
        Transaction newTransaction = Transaction.builder()
                .userId(userId)
                .transactionType(TransactionType.BUY)
                .transactionDate(request.transactionDate())
                .ticker(request.ticker())
                .quantity(request.quantity())
                .price(request.price())
                .currency(request.currency())
                .fees(request.fees())
                .tax(request.tax())
                .build();
        return transactionRepository.save(newTransaction);
    }
    private BuyTransactionResponse toBuyTransactionResponse(Position position, Transaction transaction) {
        return new BuyTransactionResponse(transaction.getId(),
                position.getId(),
                position.getTicker(),
                position.getQuantity(),
                position.getAveragePrice(),
                position.getCurrency());
    }
    private Position updateExistingPosition(BuyTransactionRequest request, Position existingPosition) {
        BigDecimal oldQuantity = existingPosition.getQuantity();
        BigDecimal oldAveragePrice = existingPosition.getAveragePrice();

        BigDecimal buyQuantity = request.quantity();
        BigDecimal buyPrice = request.price();

        BigDecimal newQuantity = oldQuantity.add(buyQuantity);
        BigDecimal oldPositionValue = oldQuantity.multiply(oldAveragePrice);
        BigDecimal buyValue = buyQuantity.multiply(buyPrice);
        BigDecimal newAveragePrice = oldPositionValue
                .add(buyValue)
                .divide(newQuantity, 6, RoundingMode.HALF_UP);
        existingPosition.setQuantity(newQuantity);
        existingPosition.setAveragePrice(newAveragePrice);
        return positionRepository.save(existingPosition);
    }
}
