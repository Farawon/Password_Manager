package org.example.password_manager_service.service.credential.update;

import org.example.password_manager_service.entities.credential.Credential;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class UpdaterFilter {
    private final CredentialUpdaterUseCase credentialUpdaterUseCase;

    public UpdaterFilter(CredentialUpdaterUseCase credentialUpdaterUseCase) {
        this.credentialUpdaterUseCase = credentialUpdaterUseCase;
    }

    public CredentialUpdateResponse filter(CredentialUpdateRequest request ) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        Credential credential = null;
        if (request.name() != null && !request.name().isEmpty()) {
            credential = credentialUpdaterUseCase.updateName(request.id(), request.name());
        }
        if(request.email() != null && !request.email().isEmpty()){
            credential = credentialUpdaterUseCase.updateEmail(request.id(), request.email());
        }
        if(request.website() !=  null && !request.website().isEmpty()){
            credential = credentialUpdaterUseCase.updateWebsite(request.id(), request.website());
        }
        if(request.categories() != null){
            credential = credentialUpdaterUseCase.updateCategories(request.id(),  request.categories());
        }
        if(request.password() != null && !request.password().isEmpty()){
            credential = credentialUpdaterUseCase.updatePassword(request.id(), request.password());
        }
        return new CredentialUpdateResponse(
                credential.getId(),
                credential.getName(),
                credential.getWebsite(),
                credential.getEmail(),
                credential.getPasswordStrength(),
                Instant.now()
                );
    }
}
