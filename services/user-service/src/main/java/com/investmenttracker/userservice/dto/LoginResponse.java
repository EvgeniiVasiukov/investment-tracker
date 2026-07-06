package com.investmenttracker.userservice.dto;

public record LoginResponse(
        String accessToken,
        String tokenType,
        UserDto user) {
}
