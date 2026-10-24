package org.example.password_manager_service.service.credential.delete;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.shared.exceptions.NotFoundCredential;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CredentialDeleteUseCase {
    private final CredentialsRepository credentialsRepository;
    public CredentialDeleteUseCase(CredentialsRepository credentialsRepository){
        this.credentialsRepository = credentialsRepository;
    }
    public void removeCredential(UUID id){
        Credential credential = credentialsRepository.findById(id).orElseThrow(NotFoundCredential::new);
        credentialsRepository.deleteById(credential.getId());
    }
}
