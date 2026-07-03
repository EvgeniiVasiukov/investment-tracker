package com.investmenttracker.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            return userId;
        }
        if (principal instanceof String userId) {
            return Long.valueOf(userId);
        }
        throw new IllegalStateException("Unsupported principal type: " + principal.getClass().getName());
    }
}
