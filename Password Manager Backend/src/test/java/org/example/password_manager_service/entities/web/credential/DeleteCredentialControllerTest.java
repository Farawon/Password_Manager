package org.example.password_manager_service.entities.web.credential;

import org.example.password_manager_service.config.SecurityConfig;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.delete.CredentialDeleteUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialProviderUseCase;
import org.example.password_manager_service.service.credential.update.UpdaterFilter;
import org.example.password_manager_service.shared.security.CredentialSecurity;
import org.example.password_manager_service.shared.security.JWT.JwtService;
import org.example.password_manager_service.web.credential.CredentialController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CredentialController.class)
@Import({SecurityConfig.class, DeleteCredentialControllerTest.CredentialSecurityTestConfig.class})
class DeleteCredentialControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CredentialCreatorUseCase credentialCreatorUseCase;
    @MockitoBean
    private CredentialDeleteUseCase credentialDeleteUseCase;
    @MockitoBean
    private CredentialProviderUseCase credentialProviderUseCase;
    @MockitoBean
    private UpdaterFilter updaterFilter;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private CredentialsRepository credentialsRepository;

    @TestConfiguration
    static class CredentialSecurityTestConfig {
        @Bean
        CredentialSecurity credentialSecurity(CredentialsRepository credentialsRepository) {
            return new CredentialSecurity(credentialsRepository);
        }
    }

    @Nested
    @DisplayName("AuthenticatedUser")
    class AuthenticatedUser {

        @Test
        @DisplayName("shouldDeleteCredential")
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        void shouldDeleteCredential() throws Exception {
            UUID id = UUID.randomUUID();
            when(credentialsRepository.existsByIdAndUser_Email(id, "test@gmail.com")).thenReturn(true);

            mockMvc.perform(delete("/credentials/{credentialId}", id))
                    .andExpect(status().isNoContent());

            verify(credentialDeleteUseCase).removeCredential(id);
        }
    }

    @Nested
    @DisplayName("UnauthenticatedUser")
    class UnauthenticatedUser {

        @Test
        @DisplayName("shouldRejectRequest")
        void shouldRejectRequest() throws Exception {
            UUID id = UUID.randomUUID();

            mockMvc.perform(delete("/credentials/{credentialId}", id))
                    .andExpect(status().isUnauthorized());
        }
    }
}
