package org.example.password_manager_service.service.credential.update;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.example.password_manager_service.shared.security.PasswordProcessor;
import org.example.password_manager_service.shared.security.passwordAnalyze.VaultPasswordResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.function.BiConsumer;


@Transactional
@Service
public class CredentialUpdaterUseCase implements CredentialUpdater{
    private final CredentialsRepository credentialsRepository;
    private final PasswordProcessor passwordProcessor;

    public CredentialUpdaterUseCase(CredentialsRepository credentialsRepository,
                                    PasswordProcessor passwordProcessor
                                    ){
        this.credentialsRepository = credentialsRepository;
        this.passwordProcessor = passwordProcessor;
    }

    private Credential getCredential(UUID id){ return credentialsRepository.findById(id).orElseThrow(UserNotFoundException::new);}

    private <T> Credential updateField(UUID id, T value, BiConsumer<Credential, T> setter){
        Credential credential = getCredential(id);
        setter.accept(credential, value);
        return credential;
    }

    @Override
    public Credential updateName(UUID id, String name){
        return updateField(id, name, Credential::setName);
    }

    @Override
    public Credential updateWebsite(UUID id, String website){
        return updateField(id, website, Credential::setWebsite);
    }

    @Override
    public Credential updateEmail(UUID id, String email){
        return updateField(id, email, Credential::setEmail);
    }

    @Override
    public Credential updateCategories(UUID id, CredentialCategories categories){return  updateField(id, categories, Credential::setCredentialCategory);}
    @Override
    public Credential updatePassword(UUID id, String rawPassword){
        Credential credential = getCredential(id);
        VaultPasswordResult result = passwordProcessor.processAndPasswordEncrypt(rawPassword);
        credential.setPasswordStrength(result.analysis().strength());
        credential.setPasswordIsLeak(result.analysis().isLeaked());
        credential.setPasswordCiphertext(result.ciphertext());
        return credential;
    }

}