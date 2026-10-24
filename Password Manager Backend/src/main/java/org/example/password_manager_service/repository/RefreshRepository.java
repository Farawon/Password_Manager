package org.example.password_manager_service.repository;

import org.example.password_manager_service.entities.user.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshRepository extends JpaRepository<UserRefreshToken, UUID> {
    Optional<UserRefreshToken> findByToken(String token);
}
