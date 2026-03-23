package com.tradetrove.backend.repository;

import com.tradetrove.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // This allows us to find a user by email during login
    Optional<User> findByEmail(String email);
}