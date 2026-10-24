package org.example.password_manager_service.shared.security.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.example.password_manager_service.service.auth_user.login_user.GeneratedRefreshToken;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class JwtService {
    private final SecretKey secretKey;
    private final long accessTokenMinutes;
    private final long refreshTokenDays;

    public JwtService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-minutes}") long accessTokenMinutes,
            @Value("${jwt.refresh-token-days}") long refreshTokenDays
    ){
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenMinutes = accessTokenMinutes;

        this.refreshTokenDays = refreshTokenDays;
    }

    public String generateAccessToken( String userName){
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenMinutes * 60 * 1000);
        return Jwts.builder()
                .subject(userName) // email
                .claim("type", "access")
                .issuedAt(now)
                .expiration(exp)
                .signWith(secretKey, Jwts.SIG.HS256) // цифровая подпись
                .compact();
    }

    public GeneratedRefreshToken generateRefreshToken(String userName) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(refreshTokenDays, ChronoUnit.DAYS);

        String token =  Jwts.builder()
                .subject(userName)
                .claim("type", "refresh")
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
        return new GeneratedRefreshToken(token, expiresAt);
    }
    public String extractUsername(String token) {
        return parseClaim(token).getSubject();
    }

    public boolean isValid(String token, UserDetails userDetails){
        try {
            Claims claims = parseClaim(token);
            if (claims.getExpiration().before(new Date())) {
                return false;
            }
            String username = claims.getSubject();
            String type = claims.get("type", String.class);
            return username.equals(userDetails.getUsername())
                    && userDetails.isEnabled()
                    && userDetails.isAccountNonExpired()
                    && userDetails.isAccountNonLocked()
                    && userDetails.isCredentialsNonExpired()
                    && "access".equals(type);
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isRefreshToken(String token) {
        // add normal test

        try {
            Claims claims = parseClaim(token);

            return "refresh".equals(
                    claims.get("type", String.class)
            );
        } catch (Exception e) {
            return false;
        }
    }

    private Claims parseClaim(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
