package com.investmenttracker.userservice.controller;

import com.investmenttracker.userservice.dto.*;
import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.User;
import com.investmenttracker.userservice.entity.UserStatus;
import com.investmenttracker.userservice.service.UserService;
import jakarta.validation.Valid;
import org.hibernate.usertype.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public PageResponse<UserDto> getAllUsers(@RequestParam (defaultValue =  "0") int page,
                                             @RequestParam (defaultValue = "20") int size,
                                             @RequestParam (defaultValue = "id") String sortBy,
                                             @RequestParam (defaultValue = "asc") String sortDirection) {
        var pageable= createPageable(page, size, sortBy, sortDirection);
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/users/filter")
    public PageResponse<UserDto> getUsersByFilter(
            @RequestParam (required = false) Role role,
            @RequestParam (required = false) UserStatus status,
            @RequestParam (required = false) String email,
            @RequestParam (defaultValue = "0") int page,
            @RequestParam (defaultValue = "20") int size,
            @RequestParam (defaultValue = "id") String sortBy,
            @RequestParam (defaultValue = "asc") String sortDirection) {
        UserFilter userFilter = new UserFilter(role, status, email);
        var pageable= createPageable(page, size, sortBy, sortDirection);
        return userService.getUsers(userFilter, pageable);
    }

    @PatchMapping("/users/{id}/activate")
    public UserDto activateUser(@PathVariable Long id) {
        return userService.activateUser(id);
    }

    @PatchMapping("/users/{id}/block")
    public UserDto blockUser(@AuthenticationPrincipal User currentUser,
                             @PathVariable Long id) {
        return userService.blockUser(currentUser.getId(), id);
    }

    @PatchMapping("/users/{id}/role")
    public UserDto changeUserRole(@AuthenticationPrincipal User currentUser,
                                  @PathVariable Long id,
                                  @Valid @RequestBody UpdateUserRoleRequest request) {
        return userService.updateUserRole(currentUser.getId(),id, request.role());
    }
    @DeleteMapping("/user/{id}")
    public UserDto deleteUser(@PathVariable Long id,
                              @AuthenticationPrincipal User currentUser) {
        return userService.deleteUser(currentUser.getId(), id);
    }
    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }
}
