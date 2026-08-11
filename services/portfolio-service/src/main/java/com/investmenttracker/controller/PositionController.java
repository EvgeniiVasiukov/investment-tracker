package com.investmenttracker.controller;

import com.investmenttracker.dto.request.CreatePositionRequest;
import com.investmenttracker.dto.response.PositionDto;
import com.investmenttracker.dto.request.PositionFilter;
import com.investmenttracker.dto.request.UpdatePostionRequest;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private PositionService positionService;
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public Page<PositionDto> getAllPositions(
            @RequestParam(required = false) String ticker,
            @RequestParam(required = false) Currency currency,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Pageable pageable) {
        PositionFilter filter = new PositionFilter(ticker, currency, minPrice, maxPrice, null);
        return positionService.getAllPositions(filter, pageable);
    }
    @GetMapping("/{id}")
    public PositionDto getPositionById(@PathVariable Long id) {
        return positionService.getPositionById(id);
    }


}
