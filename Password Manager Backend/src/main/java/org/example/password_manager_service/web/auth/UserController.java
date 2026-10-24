package org.example.password_manager_service.web.auth;


import jakarta.validation.Valid;
import org.example.password_manager_service.service.auth_user.login_user.AuthResponse;
import org.example.password_manager_service.service.auth_user.login_user.LoginUserRequest;
import org.example.password_manager_service.service.auth_user.login_user.LoginUserUseCase;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserRequest;
import org.example.password_manager_service.service.auth_user.register_user.RegisterUserUseCase;
import org.example.password_manager_service.shared.security.JWT.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {
    private final LoginUserUseCase loginUserUseCase;
    private final RefreshTokenService refreshTokenService;
    private final RegisterUserUseCase registerUserUseCase;
    public UserController(LoginUserUseCase loginUserUseCase,
                          RefreshTokenService refreshTokenService,
                          RegisterUserUseCase registerUserUseCase
                          ){
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenService = refreshTokenService;
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse loginUser(@Valid @RequestBody LoginUserRequest loginUserRequest){
        return loginUserUseCase.execute(loginUserRequest);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegisterUserRequest registerUserRequest){
        registerUserUseCase.execute(registerUserRequest);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse updateAccess(@CookieValue("refresh") String refresh){
        return refreshTokenService.refreshAccessToken(refresh);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout(@CookieValue("refresh") String refresh){
        refreshTokenService.logoutUser(refresh);
    }
}
