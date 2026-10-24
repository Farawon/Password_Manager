package org.example.password_manager_service.entities.service.credential;

import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.create.CredentialResponse;
import org.example.password_manager_service.service.credential.update.CredentialUpdateRequest;
import org.example.password_manager_service.service.credential.update.CredentialUpdateResponse;
import org.example.password_manager_service.service.credential.update.UpdaterFilter;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CredentialUpdateUseCaseTest {

    @Autowired
    private UpdaterFilter updaterFilter;

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
        @DisplayName("shouldUpdateWebsite")
        void shouldUpdateWebsite() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "Dropbox",
                    "https://dropbox.com",
                    "dropbox@gmail.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            CredentialResponse response  = credentialCreatorUseCase.createCredential(request);
            CredentialUpdateRequest  credentialUpdateRequest= new CredentialUpdateRequest( response.id(), null, "https://dropbox-updated.com", null, null, null);

            CredentialUpdateResponse updateResponse = updaterFilter.filter(credentialUpdateRequest);

            assertEquals("https://dropbox-updated.com", updateResponse.website());
        }

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldUpdateName")
        void shouldUpdateName() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "Notion",
                    "https://notion.com",
                    "notion@gmail.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            CredentialResponse response = credentialCreatorUseCase.createCredential(request);
            CredentialUpdateRequest  credentialUpdateRequest= new CredentialUpdateRequest( response.id(), "Notion Pro", null, null, null, null);

            CredentialUpdateResponse updateResponse = updaterFilter.filter( credentialUpdateRequest);

            assertEquals("Notion Pro", updateResponse.name());
        }

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldUpdateEmail")
        void shouldUpdateEmail() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "Slack",
                    "https://slack.com",
                    "slack@gmail.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            CredentialResponse response = credentialCreatorUseCase.createCredential(request);

            CredentialUpdateRequest  credentialUpdateRequest= new CredentialUpdateRequest(response.id(),null, null, "slack.updated@gmail.com", null, null);

            CredentialUpdateResponse updateResponse = updaterFilter.filter( credentialUpdateRequest);

            assertEquals("slack.updated@gmail.com", updateResponse.email());
        }

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldUpdatePassword")
        void shouldUpdatePassword() {
            CreateCredentialRequest request = new CreateCredentialRequest(
                    "GitLab",
                    "https://gitlab.com",
                    "gitlab@gmail.com",
                    "StrongPass12!@",
                    CredentialCategories.WORK
            );
            CredentialResponse response = credentialCreatorUseCase.createCredential(request);

            CredentialUpdateRequest  credentialUpdateRequest= new CredentialUpdateRequest(response.id(),null, null, null, "NewStrongPass12!@", null);
            CredentialUpdateResponse updateResponse = updaterFilter.filter(  credentialUpdateRequest);

            assertEquals(response.id(), updateResponse.id());
        }
    }

    @Nested
    @DisplayName("InvalidRequest")
    class InvalidRequest {

        @Test
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        @DisplayName("shouldRejectUnknownCredential")
        void shouldRejectUnknownCredential() {
            UUID id = UUID.randomUUID();
            CredentialUpdateRequest  credentialUpdateRequest= new CredentialUpdateRequest( id,"New Name", null, null, null, null);
            assertThrows(UserNotFoundException.class,
                    () -> updaterFilter.filter( credentialUpdateRequest));
        }
    }
}
