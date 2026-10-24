package org.example.password_manager_service.service.credential.create;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.credential.CurrentUserService;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.example.password_manager_service.shared.security.PasswordProcessor;
import org.example.password_manager_service.shared.security.passwordAnalyze.VaultPasswordResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CredentialCreatorUseCase implements CredentialCreator{
    private final CredentialFactory credentialFactory;
    private final CredentialsRepository credentialsRepository;
    private final PasswordProcessor passwordProcessor;
    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;
    public CredentialCreatorUseCase(
            CredentialFactory credentialFactory,
            CredentialsRepository credentialsRepository,
            PasswordProcessor passwordProcessor, UserRepository userRepository, CurrentUserService currentUserService
    ){
        this.credentialFactory = credentialFactory;
        this.credentialsRepository = credentialsRepository;
        this.passwordProcessor = passwordProcessor;
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Override
    public CredentialResponse createCredential(CreateCredentialRequest createCredentialRequest) {
        User user = userRepository.findByEmail(
            currentUserService.getCurrentUserEmail()
        ).orElseThrow(UserNotFoundException::new);

        VaultPasswordResult passwordResult = passwordProcessor.processAndPasswordEncrypt(createCredentialRequest.rawPassword());
        Credential credential = credentialFactory.createCredential(createCredentialRequest, passwordResult, user);
        credentialsRepository.save(credential);
        return credentialFactory.credentialResponse(createCredentialRequest,
                user.getId(),
                passwordResult.analysis().strength()
        );
    }

}
