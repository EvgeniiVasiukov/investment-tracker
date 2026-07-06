package com.investmenttracker.userservice.dto;

import com.investmenttracker.userservice.entity.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull
        UserStatus status
) {
}
