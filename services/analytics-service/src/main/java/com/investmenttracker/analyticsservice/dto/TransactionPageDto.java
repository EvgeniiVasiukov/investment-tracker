package com.investmenttracker.analyticsservice.dto;

import java.util.List;

public record TransactionPageDto(
        List<TransactionDto> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {
}
