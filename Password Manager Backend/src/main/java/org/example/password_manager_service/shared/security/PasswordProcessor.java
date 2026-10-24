package org.example.password_manager_service.shared.security;

import org.example.password_manager_service.shared.security.encrypted.PasswordEncryption;
import org.example.password_manager_service.shared.security.passwordAnalyze.AuthPasswordResult;
import org.example.password_manager_service.shared.security.passwordAnalyze.PasswordAnalysis;
import org.example.password_manager_service.shared.security.passwordAnalyze.VaultPasswordResult;
import org.example.password_manager_service.shared.validator.PasswordLevel;
import org.springframework.stereotype.Component;

@Component
public class PasswordProcessor {
    private final PasswordLevel passwordLevel;
    private final PasswordEncryption passwordEncryption;
    private final  PasswordHasher passwordHasher;
    private final CheckPasswordLeakUseCase checkPasswordLeakUseCase;
    public  PasswordProcessor(
            PasswordLevel passwordLevel,
            PasswordEncryption passwordEncryption,
            PasswordHasher passwordHasher,
            CheckPasswordLeakUseCase checkPasswordLeakUseCase

    ){
        this.passwordLevel = passwordLevel;
        this.passwordEncryption = passwordEncryption;
        this.passwordHasher = passwordHasher;
        this.checkPasswordLeakUseCase = checkPasswordLeakUseCase;
    }

    public PasswordAnalysis analyze(String rawPassword){
        return new PasswordAnalysis(
                passwordLevel.isPasswordValid(rawPassword),
                checkPasswordLeakUseCase.isPasswordLeaked(rawPassword)
        );
    }
    public VaultPasswordResult processAndPasswordEncrypt(String rawPassword){
        return new VaultPasswordResult(analyze(rawPassword), passwordEncryption.encryptedPassword(rawPassword));
    }
    public AuthPasswordResult processAndPasswordHash(String rawPassword){
        return  new AuthPasswordResult(analyze(rawPassword), passwordHasher.hash(rawPassword));
    }
}
