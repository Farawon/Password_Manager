package org.example.password_manager_service.entities.web.credential;

import org.example.password_manager_service.config.SecurityConfig;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.delete.CredentialDeleteUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialProviderUseCase;
import org.example.password_manager_service.service.credential.update.UpdaterFilter;
import org.example.password_manager_service.shared.security.JWT.JwtService;
import org.example.password_manager_service.web.credential.CredentialController;
import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CredentialController.class)
@Import(SecurityConfig.class)
public class CreateCredentialControllerTest {
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

    @Test
    @DisplayName("should return 201 when credential is valid")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void shouldCreateCredential() throws Exception {
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": "Gmail",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
                """)
                )
                .andExpect(status().isCreated());

        verify(credentialCreatorUseCase).createCredential(any());
    }
    @Test
    @DisplayName("should return 401 when user is not authenticated")
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": "Gmail",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
                """)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("should return 400 when website is invalid")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void shouldReturnBadRequestWhenWebsiteIsInvalid() throws Exception {
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": "Gmail",
                    "website": "not-a-url",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
                """)
        ).andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("should return 400 when request body is empty")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void shouldReturnBadRequestWhenBodyIsEmpty() throws Exception {
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")
        ).andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("should return 400 when JSON is malformed")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void shouldReturnBadRequestWhenJsonIsMalformed() throws Exception {
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": "Gmail",
                    "website":
                }
                """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when name is null")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void shouldReturnBadRequestWhenNameIsNull() throws Exception {
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": null,
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
                """)
        ).andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("should return 400 when request contains blank fields")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWithNullDate() throws Exception{
        mockMvc.perform(
                post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "name": " ",
                    "website": " ",
                    "email": " ",
                    "rawPassword": " ",
                    "credentialCategories": " "
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when name is blank")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongName() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": " ",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("should return 400 when website is blank")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongWebsite() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": "name",
                    "website": " ",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when email is blank")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongEmail() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": "name",
                    "website": "https://gmail.com",
                    "email": " ",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when password is blank")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongPassword() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": "name",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": " ",
                    "credentialCategories": "WORK"
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should return 400 when credentialCategories is blank")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongCredentialCategories() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                {
                    "name": "name",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": " "
                }
            """)
                )
                .andExpect(status().isBadRequest());
    }
    @Test
    @DisplayName("should return 415 when wrong content type")
    @WithMockUser(username = "test@gmail.com", roles = "USER")
    void controllerWrongContentType() throws Exception{
        mockMvc.perform(
                        post("/credentials")
                                .contentType(MediaType.TEXT_PLAIN)
                                .content("""
                {
                    "name": "name",
                    "website": "https://gmail.com",
                    "email": "email@gmail.com",
                    "rawPassword": "password213",
                    "credentialCategories": "WORK"
                }
            """)
                )
                .andExpect(status().isUnsupportedMediaType());
    }
}