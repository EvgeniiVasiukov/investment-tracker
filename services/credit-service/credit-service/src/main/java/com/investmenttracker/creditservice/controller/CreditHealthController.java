package com.investmenttracker.creditservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
        @RequestMapping("/credits")
public class CreditHealthController {
    @GetMapping("/health")
    public String health() {
        return "credit-service is up and running";
    }
}
