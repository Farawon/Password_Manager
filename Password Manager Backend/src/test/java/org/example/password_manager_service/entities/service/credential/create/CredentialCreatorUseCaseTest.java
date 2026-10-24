package org.example.password_manager_service.entities.service.credential.create;

import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CredentialResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CredentialCreatorUseCaseTest {
    @Autowired
    private CredentialCreatorUseCase credentialCreatorUseCase;

    private CreateCredentialRequest createCredentialRequest(){
        return new CreateCredentialRequest(
            "dsadsa",
            "https://dsadsa.com",
            "dsadsa@gmail.com",
                "dsaasdsdadsaA12@",
                CredentialCategories.WORK
        );
    }
    @Test
    @WithMockUser(username = "ddsaxzcsa@gmail.com", roles = "USER")
    void testCreateCredential() {
        CredentialResponse credentialResponse =  credentialCreatorUseCase.createCredential(createCredentialRequest());
        assertNotNull(credentialResponse);
    }
    // test controller if his miss null date

}
