package com.investmenttracker.service;

import com.investmenttracker.dto.request.BuyTransactionRequest;
import com.investmenttracker.dto.request.SellTransactionRequest;
import com.investmenttracker.dto.request.TransactionFilter;
import com.investmenttracker.dto.request.TransactionRequest;
import com.investmenttracker.dto.response.BuyTransactionResponse;
import com.investmenttracker.dto.response.SellTransactionResponse;
import com.investmenttracker.dto.response.TransactionResponse;
import com.investmenttracker.entity.Position;
import com.investmenttracker.entity.Transaction;
import com.investmenttracker.entity.TransactionType;
import com.investmenttracker.exception.InsufficientPositionQuantityException;
import com.investmenttracker.exception.PositionNotFoundException;
import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.repository.TransactionRepository;
import com.investmenttracker.security.SecurityUtils;
import com.investmenttracker.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Locale;
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
        String ticker = request.ticker().trim().toUpperCase(Locale.ROOT);
        Optional<Position> byTickerAndUserId = positionRepository.findByTickerAndUserId(ticker, currentUserId);
        Position result;
        if (byTickerAndUserId.isEmpty()) {
            result = createNewPosition(request, ticker, currentUserId);
        } else {
            result = updatePositionAfterBuy(request, byTickerAndUserId.get());
        }
        Transaction savedTransaction = createTransaction(request, ticker, currentUserId, TransactionType.BUY, null);
        return toBuyTransactionResponse(result, savedTransaction);
    }

    @Transactional
    public SellTransactionResponse processSell(SellTransactionRequest request) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String ticker = request.ticker().trim().toUpperCase(Locale.ROOT);
        Position existingPosition = positionRepository.findByTickerAndUserId(ticker, currentUserId)
                .orElseThrow(() -> new PositionNotFoundException("Position with ticker " + ticker + " was not found"));
        BigDecimal availableQuantity = existingPosition.getQuantity();
        BigDecimal sellQuantity = request.quantity();
        if (availableQuantity.compareTo(sellQuantity) < 0) {
            throw new InsufficientPositionQuantityException("Not enough stocks of " + ticker + " to sell");
        }
        BigDecimal realizedProfitLoss = request.price().subtract(existingPosition.getAveragePrice()).multiply(sellQuantity);
        Position result = updatePositionAfterSell(request, existingPosition);
        Transaction savedTransaction = createTransaction(request, ticker, currentUserId, TransactionType.SELL, realizedProfitLoss);
        return toSellTransactionResponse(result, savedTransaction);
    }
    public Page<TransactionResponse> getAllTransactions(TransactionFilter filter, Pageable pageable) {
        Long currentUserId = SecurityUtils.getCurrentUserId();
        String ticker = filter.ticker() == null
                ? null
                : filter.ticker().trim().toUpperCase(Locale.ROOT);

        TransactionFilter finalFilter = new TransactionFilter(
                ticker,
                filter.transactionType(),
                filter.dateFrom(),
                filter.dateTo(),
                currentUserId);
        Specification<Transaction> transactionSpecification = TransactionSpecification.byFilter(finalFilter);
        Page<Transaction> transactions = transactionRepository.findAll(transactionSpecification, pageable);
        return transactions.map(this::toTraansactionResponse);
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
    private Transaction createTransaction(TransactionRequest request, String ticker, Long userId, TransactionType transactionType, BigDecimal realizedProfitLoss) {
        Transaction newTransaction = Transaction.builder()
                .userId(userId)
                .transactionType(transactionType)
                .transactionDate(request.transactionDate())
                .ticker(ticker)
                .quantity(request.quantity())
                .price(request.price())
                .currency(request.currency())
                .fees(request.fees())
                .tax(request.tax())
                .realizedProfitLoss(realizedProfitLoss)
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
    private SellTransactionResponse toSellTransactionResponse(Position position, Transaction transaction) {
        return new SellTransactionResponse(transaction.getId(),
                position.getId(),
                position.getTicker(),
                position.getQuantity(),
                position.getAveragePrice(),
                position.getCurrency(),
                transaction.getRealizedProfitLoss());
    }
    private Position updatePositionAfterBuy(BuyTransactionRequest request, Position existingPosition) {
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
    private Position updatePositionAfterSell(SellTransactionRequest request, Position existingPosition) {
        BigDecimal remainingQuantity = existingPosition.getQuantity()
                .subtract(request.quantity());
        existingPosition.setQuantity(remainingQuantity);
        if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
            positionRepository.delete(existingPosition);
        } else {
            positionRepository.save(existingPosition);
        }   return existingPosition;
    }
    private TransactionResponse toTraansactionResponse(Transaction transaction) {
        return new TransactionResponse(transaction.getId(),
                transaction.getTransactionType(),
                transaction.getTicker(),
                transaction.getQuantity(),
                transaction.getPrice(),
                transaction.getCurrency(),
                transaction.getFees(),
                transaction.getTax(),
                transaction.getRealizedProfitLoss(),
                transaction.getTransactionDate());
    }
}
