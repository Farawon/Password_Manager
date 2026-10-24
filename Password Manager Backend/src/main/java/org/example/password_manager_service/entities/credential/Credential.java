package org.example.password_manager_service.entities.credential;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.shared.validator.model.PasswordStrength;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "Credential")
@Getter
@Setter
public class Credential {
    @Id
    @GeneratedValue(generator = "uuid7")
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "website", nullable = false)
    private String website;

    @Email(message = "Incorrect email")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password_ciphertext", nullable = false)
    private byte[] passwordCiphertext;

    @Column(name = "password_strength", nullable = false)
    private PasswordStrength passwordStrength;

    @Column(name = "password_is_leak", nullable = false)
    private boolean passwordIsLeak;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CredentialCategories credentialCategories;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;


    public void setCredentialCategory(CredentialCategories credentialCategories){
        this.credentialCategories = (credentialCategories == null) ? CredentialCategories.OTHER : credentialCategories;
    }
    public UUID getUserId(){return user.getId();}
    public byte[] getPasswordEncrypted(){return  passwordCiphertext;}

}
