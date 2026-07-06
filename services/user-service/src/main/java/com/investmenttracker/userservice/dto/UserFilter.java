package com.investmenttracker.userservice.dto;

import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.UserStatus;

public record UserFilter(
        Role role,
        UserStatus status,
        String email
) {
}
