package com.investmenttracker.userservice;

import com.investmenttracker.userservice.dto.LoginRequest;
import com.investmenttracker.userservice.dto.LoginResponse;
import com.investmenttracker.userservice.dto.UserDto;
import com.investmenttracker.userservice.dto.UserFilter;
import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.User;
import com.investmenttracker.userservice.entity.UserStatus;
import com.investmenttracker.userservice.exception.InvalidCredentialsException;
import com.investmenttracker.userservice.exception.SelfModificationNotAllowedException;
import com.investmenttracker.userservice.exception.UserIsBlockedException;
import com.investmenttracker.userservice.exception.UserNotFoundException;
import com.investmenttracker.userservice.repository.UserRepository;
import com.investmenttracker.userservice.service.JwtService;
import com.investmenttracker.userservice.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private UserService userService;

    @Test
    void blockUser_shouldThrowException_whenAdminBlocksHimself() {
        Long currentUserId = 1L;
        Long targetUserId = 1L;
        assertThrows(SelfModificationNotAllowedException.class,
                () -> userService.blockUser(currentUserId, targetUserId));
    }
    @Test
    void deleteUser_shouldThrowException_whenAdminDeletesHimself() {
        Long currentUserId = 1L;
        Long targetUserId = 1L;
        assertThrows(SelfModificationNotAllowedException.class,
                () -> userService.deleteUser(currentUserId, targetUserId));
    }
    @Test
    void changeStatus_shouldThrowException_whenAdminChangesHimselfToUser() {
        Long currentUserId = 1L;
        Long targetUserId = 1L;
        assertThrows(SelfModificationNotAllowedException.class,
                () -> userService.updateUserRole(currentUserId, targetUserId, Role.USER));
    }
    @Test
    void blockUser_status_changed_to_Block() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.ACTIVE);
        when(userRepository.findById(targetUserId)).thenReturn(
                Optional.of(targetUser));
        when(userRepository.save(targetUser)).thenReturn(targetUser);
        UserDto result = userService.blockUser(currentUserId, targetUserId);
        Assertions.assertEquals(UserStatus.BLOCKED, result.status());
        verify(userRepository).findById(targetUserId);
        verify(userRepository).save(targetUser);
    }
    @Test
    void blockUser_shouldThrowException_whenUserNotFound() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.blockUser(currentUserId, targetUserId));
    }
    @Test
    void blockUser_shouldThrowException_whenUserWasDeleted() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.DELETED);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        assertThrows(UserNotFoundException.class, () -> userService.blockUser(currentUserId, targetUserId));

    }
    @Test
    void activateUser_shouldHaveActiveStatus_whenUserActivated() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.BLOCKED);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userRepository.save(targetUser)).thenReturn(targetUser);
        UserDto result = userService.activateUser(targetUserId);
        Assertions.assertEquals(UserStatus.ACTIVE, result.status());

    }
    @Test
    void activateUser_shouldThrowException_whenUserNotFound() {
        Long targetUserId = 1L;
        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.activateUser(targetUserId));

    }
    @Test
    void deleteUser_shouldSetStatusDeleted_whenUserDeleted() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.ACTIVE);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userRepository.save(targetUser)).thenReturn(targetUser);
        UserDto result = userService.deleteUser(currentUserId, targetUserId);
        Assertions.assertEquals(UserStatus.DELETED, result.status());
        verify(userRepository).findById(targetUserId);
        verify(userRepository).save(targetUser);
    }
    @Test
    void updateUserRole_shouldChangeRole_whenUserRoleChanged() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.ACTIVE);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userRepository.save(targetUser)).thenReturn(targetUser);
        UserDto result = userService.updateUserRole(currentUserId, targetUserId, Role.ADMIN);
        Assertions.assertEquals(UserStatus.ACTIVE, result.status());
        verify(userRepository).findById(targetUserId);
        verify(userRepository).save(targetUser);
    }
    @Test
    void updateUserRole_shouldThrowException_whenUserNotFound() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(currentUserId, targetUserId, Role.ADMIN));
        verify(userRepository, never()).save(any());
    }
    @Test
    void updateUserRole_shouldThrowException_whenUserWasDeleted() {
        Long currentUserId = 1L;
        Long targetUserId = 2L;
        User targetUser = new User();
        targetUser.setId(targetUserId);
        targetUser.setEmail("test@test.com");
        targetUser.setRole(Role.USER);
        targetUser.setStatus(UserStatus.DELETED);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        assertThrows(UserNotFoundException.class, () -> userService.updateUserRole(currentUserId, targetUserId, Role.ADMIN));
        verify(userRepository).findById(targetUserId);
        verify(userRepository, never()).save(any());
    }
    @Test
    void loginUser_shouldThrowException_whenUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("test@test.com", "test");
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository).findByEmail(loginRequest.email());
        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }
    @Test
    void loginUser_shouldThrowException_whenPasswordNotMatch() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        LoginRequest loginRequest = new LoginRequest("test@test.com", "rawPassword");
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(false);
        assertThrows(InvalidCredentialsException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository).findByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), user.getPassword());
        verify(jwtService, never()).generateToken(any());

    }
    @Test
    void loginUser_shouldTrowException_whenUserBlocked() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.BLOCKED);
        LoginRequest loginRequest = new LoginRequest("test@test.com", "rawPassword");
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        assertThrows(UserIsBlockedException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository).findByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), user.getPassword());
        verify(jwtService, never()).generateToken(any());
    }
    @Test
    void loginUser_shouldTrowException_whenUserDeleted() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.DELETED);
        LoginRequest loginRequest = new LoginRequest("test@test.com", "rawPassword");
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        assertThrows(UserNotFoundException.class, () -> userService.loginUser(loginRequest));
        verify(userRepository).findByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), user.getPassword());
        verify(jwtService, never()).generateToken(any());
    }
    @Test
    void loginUser_shouldBeLoggedIn_whenPasswordAndEmailMatch() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
        LoginRequest loginRequest = new LoginRequest("test@test.com", "rawPassword");
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginRequest.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("test-token");
        LoginResponse result = userService.loginUser(loginRequest);
        Assertions.assertEquals("test-token", result.accessToken());
        Assertions.assertEquals("Bearer", result.tokenType());
        Assertions.assertEquals("test@test.com", result.user().email());
        verify(userRepository).findByEmail(loginRequest.email());
        verify(passwordEncoder).matches(loginRequest.password(), user.getPassword());
        verify(jwtService).generateToken(user);
    }
    @Test
    void getUsers_shouldReturnUsersFilteredByStatus() {
        UserFilter userFilter = new UserFilter(null, UserStatus.ACTIVE, null);
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(userPage);
        var result = userService.getUsers(userFilter, pageable);
        Assertions.assertEquals(1,result.content().size());
        Assertions.assertEquals(user.getEmail(), result.content().get(0).email());
        Assertions.assertEquals(UserStatus.ACTIVE, result.content().get(0).status());
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }
    @Test
    void getUsers_shouldReturnUsersFilteredByRole() {
        UserFilter userFilter = new UserFilter(Role.USER, null, null);
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(userPage);
        var result = userService.getUsers(userFilter, pageable);
        Assertions.assertEquals(1,result.content().size());
        Assertions.assertEquals(user.getEmail(), result.content().get(0).email());
        Assertions.assertEquals(Role.USER, result.content().get(0).role());
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }
    @Test
    void getUsers_shouldReturnUsersFilteredByEmail() {
        UserFilter userFilter = new UserFilter(null, null, "test@test.com");
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encodedPassword");
        user.setStatus(UserStatus.ACTIVE);
        user.setRole(Role.USER);
        Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(userPage);
        var result = userService.getUsers(userFilter, pageable);
        Assertions.assertEquals(1,result.content().size());
        Assertions.assertEquals(user.getEmail(), result.content().get(0).email());
        Assertions.assertEquals(Role.USER, result.content().get(0).role());
        verify(userRepository).findAll(any(Specification.class), eq(pageable));
    }

}
