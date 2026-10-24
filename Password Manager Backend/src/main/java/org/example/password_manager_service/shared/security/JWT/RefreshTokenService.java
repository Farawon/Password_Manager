package org.example.password_manager_service.shared.security.JWT;


import org.example.password_manager_service.entities.user.UserRefreshToken;
import org.example.password_manager_service.repository.RefreshRepository;
import org.example.password_manager_service.service.auth_user.login_user.AuthResponse;
import org.example.password_manager_service.shared.exceptions.IncorrectJwtToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
    private final JwtService jwtService;
    private final RefreshRepository refreshRepository;
    private final RefreshTokenUseCases refreshTokenUseCases;

    public RefreshTokenService(
            JwtService jwtService,
            RefreshRepository refreshRepository,
            RefreshTokenUseCases refreshTokenUseCases
    ) {
        this.jwtService = jwtService;
        this.refreshRepository = refreshRepository;
        this.refreshTokenUseCases = refreshTokenUseCases;
    }

    public AuthResponse refreshAccessToken(String token){
        if(!jwtService.isRefreshToken(token)){throw new IncorrectJwtToken();}
        UserRefreshToken userRefreshToken = refreshRepository.findByToken(token).orElseThrow(IncorrectJwtToken::new);
        if(userRefreshToken.isLoggedOut()){throw new IncorrectJwtToken();}
        refreshTokenUseCases.sendRefreshToken(token);
        return new AuthResponse(jwtService.generateAccessToken(jwtService.extractUsername(token)));
    }

    @Transactional
    public void logoutUser(String token){
        UserRefreshToken userToken = refreshRepository.findByToken(token).orElseThrow(IncorrectJwtToken::new);
        userToken.setLoggedOut(true);
        refreshTokenUseCases.clearRefreshToken();
    }
}
