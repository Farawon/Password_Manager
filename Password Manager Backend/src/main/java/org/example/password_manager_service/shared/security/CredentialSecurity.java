package org.example.password_manager_service.shared.security;

import org.example.password_manager_service.repository.CredentialsRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CredentialSecurity {
    private final CredentialsRepository credentialsRepository;

    public CredentialSecurity(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }


    public boolean isOwner(UUID id, Authentication authentication){
        return credentialsRepository.existsByIdAndUser_Email(id, authentication.getName());
    }

    public boolean isOwner(String userEmail, Authentication authentication){
        if (userEmail == null || authentication == null) {
            return false;
        }
        return userEmail.equals(authentication.getName());
    }
}
