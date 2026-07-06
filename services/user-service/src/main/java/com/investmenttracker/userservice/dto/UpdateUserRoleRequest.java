package com.investmenttracker.userservice.dto;

import com.investmenttracker.userservice.entity.Role;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRoleRequest(
        @NotNull
        Role role
) {
}
