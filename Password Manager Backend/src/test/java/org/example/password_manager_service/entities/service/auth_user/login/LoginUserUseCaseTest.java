package org.example.password_manager_service.entities.service.auth_user.login;


import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.service.auth_user.login_user.AuthResponse;
import org.example.password_manager_service.service.auth_user.login_user.LoginUserRequest;
import org.example.password_manager_service.service.auth_user.login_user.LoginUserUseCase;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LoginUserUseCaseTest {
    @Autowired
    private LoginUserUseCase loginUserUseCase;

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

    @Test
    void loginUser() {
        LoginUserRequest user = new LoginUserRequest("test@gmail.com", "LierrrtF231@!*");

        AuthResponse response = loginUserUseCase.execute(user);

        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getAccessToken(), "Access token should not be null");
    }
}
