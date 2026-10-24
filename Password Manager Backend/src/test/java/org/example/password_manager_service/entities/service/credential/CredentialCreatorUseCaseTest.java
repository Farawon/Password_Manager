package org.example.password_manager_service.entities.service.credential;

import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CredentialResponse;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CredentialCreatorUseCaseTest {

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

    private CreateCredentialRequest validRequest(String email) {
        return new CreateCredentialRequest(
                "Google",
                "https://google.com",
                email,
                "StrongPass12!@",
                CredentialCategories.WORK
        );
    }

    @Nested
    @DisplayName("AuthenticatedUser")
    class AuthenticatedUser {

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldCreateCredential")
        void shouldCreateCredential() {
            String email = "test@gmail.com";

            CredentialResponse response = credentialCreatorUseCase.createCredential(validRequest(email));

            assertNotNull(response);
            assertNotNull(response.id());
            assertEquals("Google", response.name());
            assertEquals("https://google.com", response.website());
            assertEquals(email, response.email());
        }
    }

    @Nested
    @DisplayName("UnauthenticatedUser")
    class UnauthenticatedUser {

        @Test
        @DisplayName("shouldRejectRequest")
        void shouldRejectRequest() {
            String email = "test@gmail.com";

            assertThrows(Exception.class, () -> credentialCreatorUseCase.createCredential(validRequest(email)));
        }
    }

    @Nested
    @DisplayName("InvalidRequest")
    class InvalidRequest {

        @Test
        @WithMockUser(username = "ghost-user@test.com", roles = "USER")
        @DisplayName("shouldRejectUnknownUser")
        void shouldRejectUnknownUser() {
            String email = "ghost-user@test.com";
            CreateCredentialRequest request = validRequest(email);
            assertThrows(UserNotFoundException.class,
                    () -> credentialCreatorUseCase.createCredential(request));
        }
    }
}
