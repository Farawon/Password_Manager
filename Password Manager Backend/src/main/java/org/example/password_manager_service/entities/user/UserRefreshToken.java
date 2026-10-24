package org.example.password_manager_service.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_refresh_token")
@Getter
@Setter
public class UserRefreshToken {
    @Id
    @GeneratedValue(generator = "uuid7")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "is_logged_out")
    private boolean isLoggedOut;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
    }}
