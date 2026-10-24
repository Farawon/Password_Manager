package org.example.password_manager_service.entities.service.credential;

import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.delete.CredentialDeleteUseCase;
import org.example.password_manager_service.shared.exceptions.NotFoundCredential;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CredentialDeleteUseCaseTest {

    @Autowired
    private CredentialDeleteUseCase credentialDeleteUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private CredentialCreatorUseCase credentialCreatorUseCase;

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @BeforeAll
    static void setupUser(@Autowired RegisterUserUseCase registerUserUseCase,
                          @Autowired UserRepository userRepository) {
        if (!userRepository.existsByEmail("test@gmail.com")) {
            RegisterUserRequest registerUser = new RegisterUserRequest(
                    "Test", "User", "test@gmail.com", "LimerrrafF231@@!", "Linerak2sa241s313!",
                    true
            );
            registerUserUseCase.execute(registerUser);
        }
    }

    @Nested
    @DisplayName("AuthenticatedUser")
    class AuthenticatedUser {

        @Test
        @DisplayName("shouldDeleteCredential")
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        void shouldDeleteCredential() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "Dropbox",
                    "https://dropbox.com",
                    "dropbox@test.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            var response = credentialCreatorUseCase.createCredential(request);

            credentialDeleteUseCase.removeCredential(response.id());

            assertTrue(credentialsRepository.findById(response.id()).isEmpty());
        }
    }

    @Nested
    @DisplayName("InvalidRequest")
    class InvalidRequest {

        @Test
        @DisplayName("shouldThrowWhenCredentialDoesNotExist")
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        void shouldThrowWhenCredentialDoesNotExist() {
            UUID id = UUID.randomUUID();
            assertThrows(NotFoundCredential.class,
                    () -> credentialDeleteUseCase.removeCredential(id));
        }
    }
}
