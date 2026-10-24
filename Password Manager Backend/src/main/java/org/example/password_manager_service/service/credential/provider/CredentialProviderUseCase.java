package org.example.password_manager_service.service.credential.provider;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.shared.exceptions.IncorrectCredential;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.example.password_manager_service.shared.security.encrypted.PasswordEncryption;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@Transactional(readOnly = true)
public class CredentialProviderUseCase implements CredentialProvider {
    private final CredentialsRepository credentialsRepository;
    private final PasswordEncryption passwordEncryption;
    public CredentialProviderUseCase(CredentialsRepository credentialsRepository, PasswordEncryption passwordEncryption){
        this.credentialsRepository = credentialsRepository;
        this.passwordEncryption = passwordEncryption;
    }

    private CredentialResponseDto convertToDto(Credential credential){
        return new CredentialResponseDto(
                credential.getId(),
                credential.getName(),
                credential.getWebsite(),
                credential.getEmail(),
                credential.getPasswordStrength(),
                credential.getCredentialCategories(),
                credential.getUpdatedAt()
        );
    }



    @Override
    public List<CredentialResponseDto> credentialsProvider(String  email){
        return credentialsRepository.findAllByUser_Email(email).stream().map(this::convertToDto).toList();
    }
    @Override
    public CredentialResponseDto credentialProvider(UUID id){
        Credential credential  = credentialsRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return convertToDto(credential);
    }
    @Override
    public List<CredentialResponseDto> credentialProviderCategory( CredentialCategories credentialCategories){
        return credentialsRepository.findByCredentialCategory(credentialCategories).stream().map(this::convertToDto).toList();
    }

    @Override
    public String getCredentialPassword(UUID id){
        Credential credential = credentialsRepository.findById(id).orElseThrow(IncorrectCredential::new);

        return passwordEncryption.decryptedPassword(credential.getPasswordEncrypted());
    }
}
