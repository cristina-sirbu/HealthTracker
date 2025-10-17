package com.healthtracker.healthtracker.user.infra;

import java.util.Optional;

import com.healthtracker.healthtracker.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
