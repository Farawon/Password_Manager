package org.example.password_manager_service.service.credential.create;


    import org.example.password_manager_service.entities.credential.CredentialCategories;
    import org.example.password_manager_service.shared.validator.model.PasswordStrength;

    import java.time.Instant;
    import java.util.UUID;

    public record CredentialResponse (
            UUID id,
            String name,
            String website,
            String email,
            CredentialCategories credentialCategories,
            PasswordStrength passwordStrength,
            Instant createdAt
    ){
    }
