package com.investmenttracker.service;

import com.investmenttracker.repository.PositionRepository;
import com.investmenttracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PositionRepository positionRepository;

    public TransactionService(TransactionRepository transactionRepository, PositionRepository positionRepository) {
        this.transactionRepository = transactionRepository;
        this.positionRepository = positionRepository;
    }
}
