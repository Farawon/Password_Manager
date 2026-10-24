package org.example.password_manager_service.service.credential.update;

import org.example.password_manager_service.shared.validator.model.PasswordStrength;

import java.time.Instant;
import java.util.UUID;

public record CredentialUpdateResponse (
    UUID id,
    String name,
    String website,
    String email,
    PasswordStrength passwordStrength,
    Instant updatedAt
){}
