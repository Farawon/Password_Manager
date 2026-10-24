package org.example.password_manager_service.entities.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Users",
        indexes = {
            @Index(name = "idx_user_email", columnList = "email")
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(generator = "uuid7")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus = UserStatus.ACTIVE;
    // We add later admin panel, for edit user status 

    @Enumerated(EnumType.STRING)
    private UserSubscription userSubscription = UserSubscription.FREE;

    @Email(message = "Incorrect email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "stripe_customer_id", nullable = false, unique = true)
    private String stripeCustomerId;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "is_terms_accepted", nullable = false)
    private Boolean isTermsAccepted;

    @JsonIgnore
    public String getPassword() {
        return hashedPassword;
    }


    @JsonIgnore
    public List<GrantedAuthority> getRoleList() {
        return List.of(
                new SimpleGrantedAuthority(role.name())
        );
    }
}