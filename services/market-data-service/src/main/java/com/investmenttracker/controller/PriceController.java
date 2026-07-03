package com.investmenttracker.controller;

import com.investmenttracker.dto.PriceDto;
import com.investmenttracker.service.PriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prices")
public class PriceController {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{ticker}")
    public PriceDto getPrice(@PathVariable("ticker") String ticker) {
        return priceService.getPrice(ticker);
    }
}
