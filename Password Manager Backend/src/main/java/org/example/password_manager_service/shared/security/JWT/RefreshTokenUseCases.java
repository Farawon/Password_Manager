package org.example.password_manager_service.shared.security.JWT;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Duration;

@Component
public class RefreshTokenUseCases {
    private static final String COOKIE_NAME = "refresh";

    private final long refreshTokenDays;
    private final boolean cookieSecure;
    private final String cookieSameSite;
    private final boolean cookiePartitioned;

    public RefreshTokenUseCases(
            @Value("${jwt.refresh-token-days}") long refreshTokenDays,
            @Value("${app.cookie.secure:true}") boolean cookieSecure,
            @Value("${app.cookie.same-site:None}") String cookieSameSite,
            @Value("${app.cookie.partitioned:true}") boolean cookiePartitioned
    ) {
        this.refreshTokenDays = refreshTokenDays;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
        this.cookiePartitioned = cookiePartitioned;
    }

    public void sendRefreshToken(String refresh) {
        write(buildCookie(refresh, Duration.ofDays(refreshTokenDays)));
    }

    public void clearRefreshToken() {
        write(buildCookie("", Duration.ZERO));
    }

    private ResponseCookie buildCookie(String value, Duration maxAge) {
        boolean none = "none".equalsIgnoreCase(cookieSameSite);
        return ResponseCookie.from(COOKIE_NAME, value)
                .httpOnly(true)
                .secure(cookieSecure || none)
                .path("/")
                .maxAge(maxAge)
                .sameSite(cookieSameSite)
                .partitioned(cookiePartitioned || none)
                .build();
    }

    private void write(ResponseCookie cookie) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        if (response == null) {
            throw new IllegalStateException("No HTTP response available to write refresh cookie");
        }
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
