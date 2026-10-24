package org.example.password_manager_service.shared.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

@Component
public class PasswordHasher {
    private static final int ITERATIONS = 3;
    private static final int MEMORY = 65536;
    private static final int PARALLELISM = 1;

    private final Argon2 argon2 = Argon2Factory.create(
            Argon2Factory.Argon2Types.ARGON2id
    );

    public String hash(String password) {
        char[] chars = password.toCharArray();

        try {
            return argon2.hash(ITERATIONS, MEMORY, PARALLELISM, chars);
        } finally {
            argon2.wipeArray(chars);
        }
    }

    public boolean verify(String rawPassword, String storedPhcHash) {
        char[] chars = rawPassword.toCharArray();

        try {
            return argon2.verify(storedPhcHash, chars);
        } finally {
            argon2.wipeArray(chars);
        }
    }
}
