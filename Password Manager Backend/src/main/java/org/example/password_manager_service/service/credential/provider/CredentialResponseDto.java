package org.example.password_manager_service.service.credential.provider;


import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.shared.validator.model.PasswordStrength;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CredentialResponseDto (
    UUID id,
    String name,
    String website,
    String email,
    PasswordStrength passwordStrength,
    CredentialCategories credentialCategories,
    OffsetDateTime updatedAt
){}
