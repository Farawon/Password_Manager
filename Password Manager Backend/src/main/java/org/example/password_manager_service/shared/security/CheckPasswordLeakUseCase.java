package org.example.password_manager_service.shared.security;

import org.example.password_manager_service.shared.geteway.HibpGateway;
import org.example.password_manager_service.shared.utility.HexConverter;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.List;

@Component
public class CheckPasswordLeakUseCase {
    private final Sha1PasswordHasher sha1PasswordHasher;
    private final HexConverter hexConverter;
    private final HibpGateway hibpGateway;

    public CheckPasswordLeakUseCase(Sha1PasswordHasher sha1PasswordHasher, HexConverter hexConverter, HibpGateway hibpGateway){
        this.sha1PasswordHasher = sha1PasswordHasher;
        this.hexConverter = hexConverter;
        this.hibpGateway = hibpGateway;
    }

    public boolean isPasswordLeaked(String password){
        try {
            byte[] encryption = sha1PasswordHasher.encryptionShaOne(password);
            String hexPass = hexConverter.hexFormat(encryption).toUpperCase();
            String prefix = hexPass.substring(0, 5);
            String suffix = hexPass.substring(5);

            List<String> listPass = hibpGateway.pwnedApiClient(prefix);

            return listPass.contains(suffix);
        }catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-1 algorithm is unavailable", e);
        }
    }
}
