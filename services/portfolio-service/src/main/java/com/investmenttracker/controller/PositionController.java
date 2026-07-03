package com.investmenttracker.controller;

import com.investmenttracker.dto.CreatePositionRequest;
import com.investmenttracker.dto.PositionDto;
import com.investmenttracker.dto.PositionFilter;
import com.investmenttracker.dto.UpdatePostionRequest;
import com.investmenttracker.entity.Currency;
import com.investmenttracker.service.PositionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private PositionService positionService;
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PositionDto createPosition(@Valid @RequestBody CreatePositionRequest request) {
        return positionService.createPosition(request);
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
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePositionById(@PathVariable Long id) {
        positionService.deletePositionById(id);
    }
    @PatchMapping("/{id}")
    public PositionDto updatePositionById(@PathVariable Long id,
                                          @Valid @RequestBody UpdatePostionRequest request) {
        return positionService.updatePosition(id, request);
    }
}
