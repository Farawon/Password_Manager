package org.example.password_manager_service.service.auth_user.login_user;

import java.time.Instant;

public record GeneratedRefreshToken(
        String token,
        Instant expiresAt
) {}