package org.example.password_manager_service.service.credential.create;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.shared.security.passwordAnalyze.VaultPasswordResult;
import org.example.password_manager_service.shared.validator.model.PasswordStrength;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class CredentialFactory {
    public Credential createCredential(CreateCredentialRequest createCredentialRequest,
                                       VaultPasswordResult passwordAnalysis, User user
    ){
        Credential credential = new Credential();
        credential.setName(createCredentialRequest.name());
        credential.setWebsite(createCredentialRequest.website());
        credential.setEmail(createCredentialRequest.email());
        credential.setUser(user);
        credential.setPasswordCiphertext(passwordAnalysis.ciphertext());
        credential.setPasswordStrength(passwordAnalysis.analysis().strength());
        credential.setPasswordIsLeak(passwordAnalysis.analysis().isLeaked());
        credential.setCredentialCategory(createCredentialRequest.credentialCategories());
        return credential;
    }
    public CredentialResponse credentialResponse(CreateCredentialRequest createCredentialRequest,
                                                       UUID id,
                                                       PasswordStrength passwordStrength
    ){
        return new CredentialResponse(
                id,
                createCredentialRequest.name(),
                createCredentialRequest.website(),
                createCredentialRequest.email(),
                createCredentialRequest.credentialCategories(),
                passwordStrength,
                Instant.now()
        );
    }
}
