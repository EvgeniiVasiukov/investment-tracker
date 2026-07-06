package com.investmenttracker.userservice;

import com.investmenttracker.userservice.dto.UserFilter;
import com.investmenttracker.userservice.entity.Role;
import com.investmenttracker.userservice.entity.User;
import com.investmenttracker.userservice.entity.UserStatus;
import com.investmenttracker.userservice.repository.UserRepository;
import com.investmenttracker.userservice.specification.UserSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class UserSpecificationTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    void byFilter_shouldFilterByStatus() {
        User user1 = new User(null, "test@test.de", "somePassword", Role.USER, UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, "test@test2.de", "pass", Role.USER, UserStatus.BLOCKED, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        UserFilter userFilter = new UserFilter(null, UserStatus.ACTIVE, null);
        List<User> all = userRepository.findAll(UserSpecification.byFilter(userFilter));
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(UserStatus.ACTIVE, all.get(0).getStatus());
        Assertions.assertEquals("test@test.de", all.get(0).getEmail());
    }
    @Test
    void byFilter_shouldFilterByRole() {
        User user1 = new User(null, "mocked@test.de", "pass", Role.USER, UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, "wow@test.de", "pass", Role.ADMIN, UserStatus.BLOCKED, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        UserFilter userFilter = new UserFilter(Role.USER, null, null);
        List<User> all = userRepository.findAll(UserSpecification.byFilter(userFilter));
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(Role.USER, all.get(0).getRole());
        Assertions.assertEquals("mocked@test.de", all.get(0).getEmail());
    }
    @Test
    void byFilter_shouldFilterByEmail() {
        User user1 = new User(null, "mocked@test.de", "pass", Role.USER, UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, "wow@test.de", "pass", Role.ADMIN, UserStatus.BLOCKED, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        UserFilter userFilter = new UserFilter(null, null, "mocked@test.de");
        List<User> all = userRepository.findAll(UserSpecification.byFilter(userFilter));
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(Role.USER, all.get(0).getRole());
        Assertions.assertEquals(UserStatus.ACTIVE, all.get(0).getStatus());
        Assertions.assertEquals("mocked@test.de", all.get(0).getEmail());
    }
    @Test
    void byFilter_shouldFilterByRoleAndStatus() {
        User user1 = new User(null, "mocked@test.de", "pass", Role.ADMIN, UserStatus.BLOCKED, LocalDateTime.now(), LocalDateTime.now());
        User user2 = new User(null, "wow@test.de", "pass2", Role.ADMIN, UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        User user3 = new User(null, "mocked2@test.de", "pass3", Role.USER, UserStatus.ACTIVE, LocalDateTime.now(), LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        UserFilter userFilter = new UserFilter(Role.ADMIN, UserStatus.ACTIVE, null);
        List<User> all = userRepository.findAll(UserSpecification.byFilter(userFilter));
        Assertions.assertEquals(1, all.size());
        Assertions.assertEquals(Role.ADMIN, all.get(0).getRole());
        Assertions.assertEquals(UserStatus.ACTIVE, all.get(0).getStatus());
        Assertions.assertEquals("wow@test.de", all.get(0).getEmail());
    }
}
