package org.example.password_manager_service.entities.service.auth_user.register;

import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class RegisterUserUseCaseTest {

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void registerUserTest(){
        RegisterUserRequest registerUser = new RegisterUserRequest(
                "Test", "User", "test@gmail.com", "LimerrrafF231@@!", "Linerak2sa241s313!",
                 true
        );
        assertNotNull(registerUser);
    }
}

