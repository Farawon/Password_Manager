package org.example.password_manager_service.shared.security.passwordAnalyze;

import org.example.password_manager_service.shared.validator.model.PasswordStrength;

public record PasswordAnalysis(
        PasswordStrength strength,
        boolean isLeaked
) { }
