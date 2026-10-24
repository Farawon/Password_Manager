package org.example.password_manager_service.entities.service.credential;

import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.provider.CredentialProviderUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialResponseDto;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CredentialProviderUseCaseTest {

    @Autowired
    private CredentialProviderUseCase credentialProviderUseCase;

    @Autowired
    private CredentialCreatorUseCase credentialCreatorUseCase;

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private UserRepository userRepository;

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
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldReturnCredentialById")
        void shouldReturnCredentialById() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "GitHub",
                    "https://github.com",
                    "github@test.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            var response = credentialCreatorUseCase.createCredential(request);

            CredentialResponseDto dto = credentialProviderUseCase.credentialProvider(response.id());

            assertEquals(response.id(), dto.id());
            assertEquals("GitHub", dto.name());
        }

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldReturnAllCredentialsForCurrentUser")
        void shouldReturnAllCredentialsForCurrentUser() {
            CreateCredentialRequest firstRequest = new CreateCredentialRequest(
                    "Drive",
                    "https://drive.com",
                    "drive@test.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            var first = credentialCreatorUseCase.createCredential(firstRequest);

            CreateCredentialRequest secondRequest = new CreateCredentialRequest(
                    "Slack",
                    "https://slack.com",
                    "slack@test.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            var second = credentialCreatorUseCase.createCredential(secondRequest);

            List<CredentialResponseDto> credentials = credentialProviderUseCase.credentialsProvider("test@gmail.com");

            assertFalse(credentials.isEmpty());
            assertEquals(2, credentials.size());
            assertEquals(List.of(first.id(), second.id()).size(), credentials.stream().map(CredentialResponseDto::id).distinct().count());
        }
    }

    @Nested
    @DisplayName("InvalidRequest")
    class InvalidRequest {

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldRejectUnknownCredentialId")
        void shouldRejectUnknownCredentialId() {
            UUID id = UUID.randomUUID();
            assertThrows(UserNotFoundException.class,
                    () -> credentialProviderUseCase.credentialProvider(id));
        }
    }
}
