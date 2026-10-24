package org.example.password_manager_service.service.credential.provider;


import org.example.password_manager_service.entities.credential.CredentialCategories;

import java.util.List;
import java.util.UUID;

public interface CredentialProvider {
    List<CredentialResponseDto> credentialsProvider(String  email);
    CredentialResponseDto credentialProvider(UUID id);
    List<CredentialResponseDto> credentialProviderCategory(CredentialCategories credentialCategories);
    String getCredentialPassword(UUID id);
}
