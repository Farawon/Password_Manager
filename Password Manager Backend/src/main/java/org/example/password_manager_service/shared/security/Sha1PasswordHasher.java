package org.example.password_manager_service.shared.security;


import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
@Component
@SuppressWarnings("java:S4790")
public class Sha1PasswordHasher {
    private static final String ALGORITHM_ENCRYPTION = "SHA-1";

    public byte[] encryptionShaOne(String password) throws NoSuchAlgorithmException {
        MessageDigest digest  = MessageDigest.getInstance(ALGORITHM_ENCRYPTION);
        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
    }
}
