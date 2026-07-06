package com.investmenttracker.userservice.service;


import com.investmenttracker.userservice.dto.*;
import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.User;
import com.investmenttracker.userservice.entity.UserStatus;
import com.investmenttracker.userservice.exception.*;
import com.investmenttracker.userservice.repository.UserRepository;
import com.investmenttracker.userservice.specification.UserSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    private UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
    private <T> PageResponse<T> toPageResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    public UserDto getCurrentUserDto(User user) {
        return toDto(user);
    }

    public PageResponse<UserDto> getAllUsers(Pageable pageable) {
        var users = userRepository
                .findAll(pageable)
                .map(this::toDto);
        return toPageResponse(users);
    }
    public PageResponse<UserDto> getUsers(UserFilter filter, Pageable pageable) {
        Specification<User> specification = UserSpecification.byFilter(filter);
        return toPageResponse(userRepository.findAll(specification, pageable).map(this::toDto));
    }
    public UserDto registerUser(RegisterRequest request) {
            Optional<User> response = userRepository.findByEmail(request.email());
            if (response.isPresent()) {
                throw new UserAlreadyExistsException(String.format("User with email %s already exists", request.email()));
            }
        User user = new User();
            user.setEmail(request.email());
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setRole(Role.USER);
            user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }
    public LoginResponse loginUser(LoginRequest request) {
        Optional<User> response = userRepository.findByEmail(request.email());
        if (!response.isPresent()) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        User user = response.get();
        var rawPassword = request.password();
        var encodedPasswordFromDb = user.getPassword();
        if (!passwordEncoder.matches(rawPassword, encodedPasswordFromDb)) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new UserIsBlockedException("User is blocked");
        }
        if (user.getStatus() == UserStatus.DELETED) {
            throw new UserNotFoundException("User not found");
        }
        var token = jwtService.generateToken(user);
        return new LoginResponse(token, "Bearer", toDto(user));
    }
    private UserDto updateUserStatus(Long userId, UserStatus status) {
                var user = getUserOrThrow(userId);
                user.setStatus(status);
                User savedUser = userRepository.save(user);
                return toDto(savedUser);
    }

    public UserDto blockUser(Long currentUserId, Long targetUserId) {
        validateNoSelfModification(currentUserId, targetUserId);
       return updateUserStatus(targetUserId, UserStatus.BLOCKED);
    }

    public UserDto deleteUser(Long currentUserId, Long targetUserId) {
        validateNoSelfModification(currentUserId, targetUserId);
        return updateUserStatus(targetUserId, UserStatus.DELETED);
    }

    public UserDto activateUser(Long userId) {
        return updateUserStatus(userId, UserStatus.ACTIVE);
    }
    public UserDto updateUserRole(Long currentUserId, Long targetUserId, Role role) {
        validateNoSelfModification(currentUserId, targetUserId);
        var user = getUserOrThrow(targetUserId);
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    private void validateNoSelfModification(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            throw new SelfModificationNotAllowedException("Self Modification not allowed");
        }
    }
    private User getUserOrThrow(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id" + userId + " not found"));
        if (user.getStatus() == UserStatus.DELETED) {
            throw new UserNotFoundException("User with id" + userId + " not found");
        }
        return user;
    }

}
