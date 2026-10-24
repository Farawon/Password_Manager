package org.example.password_manager_service.entities.web.credential;

import org.example.password_manager_service.config.SecurityConfig;
import org.example.password_manager_service.repository.CredentialsRepository;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.delete.CredentialDeleteUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialProviderUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialResponseDto;
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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CredentialController.class)
@Import({SecurityConfig.class, ProviderCredentialControllerTest.CredentialSecurityTestConfig.class})
class ProviderCredentialControllerTest {

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
        @DisplayName("shouldReturnCredentialById")
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        void shouldReturnCredentialById() throws Exception {
            UUID id = UUID.randomUUID();
            String text = "2026-10-20T12:34:56+02:00";
            OffsetDateTime parsedDate = OffsetDateTime.parse(text);
            CredentialResponseDto dto = new CredentialResponseDto(
                    id,
                    "Gmail",
                    "https://gmail.com",
                    "test@gmail.com",
                    org.example.password_manager_service.shared.validator.model.PasswordStrength.STRONG,
                    org.example.password_manager_service.entities.credential.CredentialCategories.WORK,
                    parsedDate

            );

            when(credentialsRepository.existsByIdAndUser_Email(id, "test@gmail.com")).thenReturn(true);
            when(credentialProviderUseCase.credentialProvider(id)).thenReturn(dto);

            mockMvc.perform(get("/credentials/{id}", id))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("shouldReturnAllCredentialsForUser")
        @WithMockUser(username = "test@gmail.com", roles = "USER")
        void shouldReturnAllCredentialsForUser() throws Exception {
            when(credentialProviderUseCase.credentialsProvider(anyString()))
                    .thenReturn(List.of());

            mockMvc.perform(get("/credentials").param("userEmail", "test@gmail.com"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("UnauthenticatedUser")
    class UnauthenticatedUser {

        @Test
        @DisplayName("shouldRejectRequest")
        void shouldRejectRequest() throws Exception {
            UUID id = UUID.randomUUID();

            mockMvc.perform(get("/credentials/{id}", id))
                    .andExpect(status().isUnauthorized());
        }
    }
}
