package com.investmenttracker.userservice.dto;

import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.UserStatus;

import java.time.LocalDateTime;

public record UserDto(
        Long id,
        String email,
        Role role,
        UserStatus status,
        LocalDateTime createdAt
) {
}
