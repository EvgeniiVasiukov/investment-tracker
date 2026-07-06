package com.investmenttracker.userservice.dto;

import lombok.Getter;

import java.time.LocalDateTime;

public record ErrorResponse (
        String message,
        LocalDateTime timeStamp
){
}
