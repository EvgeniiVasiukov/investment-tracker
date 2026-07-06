package com.investmenttracker.userservice.controller;

import com.investmenttracker.userservice.dto.UserDto;
import com.investmenttracker.userservice.entity.User;
import com.investmenttracker.userservice.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserDto me(@AuthenticationPrincipal User user) {
        return userService.getCurrentUserDto(user);
    }

}