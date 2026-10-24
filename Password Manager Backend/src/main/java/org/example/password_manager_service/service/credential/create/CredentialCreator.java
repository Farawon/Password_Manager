package org.example.password_manager_service.service.credential.create;

public interface CredentialCreator {
    CredentialResponse createCredential(CreateCredentialRequest createCredentialRequest);
}
