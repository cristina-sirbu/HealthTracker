package com.healthtracker.healthtracker.user.app;

import com.healthtracker.healthtracker.user.infra.UserRepository;
import com.healthtracker.healthtracker.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    // What @Transactional means?
    // All the database operations inside this method should run in a single transaction — if any of them fail, rollback everything.
    public User register(String username, String password) {
        if (username==null || username.isBlank() || password==null || password.isBlank()) {
            throw new IllegalArgumentException("Username and password must not be null");
        }
        if(userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already taken");
        }

        try {
            User u = new User();
            u.setUsername(username);
            u.setPassword(passwordEncoder.encode(password));
            return userRepository.save(u);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Username already exists");
        }
    }

    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username).filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}
