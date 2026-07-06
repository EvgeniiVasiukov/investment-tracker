package com.investmenttracker.userservice.controller;

import com.investmenttracker.userservice.dto.LoginRequest;
import com.investmenttracker.userservice.dto.LoginResponse;
import com.investmenttracker.userservice.dto.RegisterRequest;
import com.investmenttracker.userservice.dto.UserDto;
import com.investmenttracker.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserDto register(@RequestBody @Valid RegisterRequest registerRequest) {
        return userService.registerUser(registerRequest);
    }
    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

}
