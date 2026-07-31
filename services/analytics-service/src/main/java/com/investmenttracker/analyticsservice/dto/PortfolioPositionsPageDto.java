package com.investmenttracker.analyticsservice.dto;

import java.util.List;

public record PortfolioPositionsPageDto(
        List<PortfolioPositionDto> content,
        int number,
        int size,
        long totalElements,
        int totalPages
) {
}
