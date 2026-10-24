package org.example.password_manager_service.shared.security.passwordAnalyze;

public record AuthPasswordResult(
        PasswordAnalysis analysis,
        String phcHash
){}
