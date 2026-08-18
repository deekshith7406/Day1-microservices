package com.oneenterprise.userservice.service;

import com.oneenterprise.userservice.dto.UserDto;
import com.oneenterprise.userservice.exception.UserNotFoundException;
import com.oneenterprise.userservice.model.User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Owns user data. Per the Day 1 handbook, this is intentionally in-memory —
 * no database — so the exercise stays focused on service boundaries and
 * HTTP communication rather than persistence.
 */
@Service
public class UserService {

    private final Map<Long, User> users = new ConcurrentHashMap<>();

    public UserService() {
        // Seed some sample data
        users.put(1L, new User(1L, "Deekshith", "Deekshith@example.com", "ACTIVE"));
        users.put(2L, new User(2L, "samara", "samara@example.com", "ACTIVE"));
        users.put(3L, new User(3L, "reddy", "reddy@example.com", "SUSPENDED"));
    }

    public UserDto getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return toDto(user);
    }

    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getFullName(), user.getEmail());
    }
}
