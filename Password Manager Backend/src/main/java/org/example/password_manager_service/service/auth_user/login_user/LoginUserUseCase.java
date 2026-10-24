package org.example.password_manager_service.service.auth_user.login_user;


import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.entities.user.UserRefreshToken;
import org.example.password_manager_service.repository.RefreshRepository;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.shared.exceptions.InvalidEmailOrPassword;
import org.example.password_manager_service.shared.security.JWT.JwtService;
import org.example.password_manager_service.shared.security.JWT.RefreshTokenUseCases;
import org.example.password_manager_service.shared.security.PasswordHasher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class LoginUserUseCase implements LoginUser {
    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final JwtService jwtService;
    private final RefreshTokenUseCases refreshTokenUseCases;
    private final RefreshRepository refreshRepository;
    public LoginUserUseCase(UserRepository userRepository, PasswordHasher passwordHasher, JwtService jwtService, RefreshTokenUseCases refreshTokenUseCases, RefreshRepository refreshRepository){
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.jwtService = jwtService;
        this.refreshTokenUseCases = refreshTokenUseCases;
        this.refreshRepository = refreshRepository;
    }
    private UserRefreshToken userRefreshToken(GeneratedRefreshToken generated , User user){
        UserRefreshToken userRefreshToken = new UserRefreshToken();
        userRefreshToken.setToken(generated.token());
        userRefreshToken.setUser(user);
        userRefreshToken.setExpiresAt(generated.expiresAt());
        return  userRefreshToken;
    }
    public AuthResponse execute(LoginUserRequest request ){
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new InvalidEmailOrPassword("Invalid email or password")); // InvalidCredentialsException
        if(!passwordHasher.verify(request.rawPassword(), user.getPassword())){
            throw new InvalidEmailOrPassword("Invalid email or password"); // add own exemption(исключение)
        }

        String access = jwtService.generateAccessToken(user.getEmail());
        GeneratedRefreshToken refresh = jwtService.generateRefreshToken(user.getEmail());
        refreshTokenUseCases.sendRefreshToken(refresh.token());
        refreshRepository.save(userRefreshToken(refresh, user));

        return new AuthResponse(access);
    }
}
