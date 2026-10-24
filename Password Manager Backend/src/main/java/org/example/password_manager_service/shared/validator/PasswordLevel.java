package org.example.password_manager_service.shared.validator;

import org.example.password_manager_service.shared.validator.model.PasswordStrength;
import org.springframework.stereotype.Component;

@Component
public class PasswordLevel {
    // I would use zxcvbn4j, but I want to write this, i know this is  shit but this is my shit)

    public PasswordStrength isPasswordValid(String password) {
        if (password == null) return PasswordStrength.WEAK;

        boolean hasDigit = false;
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasSpecial = false;

        for (char ch : password.toCharArray()) {
            if (Character.isDigit(ch)) hasDigit = true;
            else if (Character.isLowerCase(ch)) hasLower = true;
            else if (Character.isUpperCase(ch)) hasUpper = true;
            else if (!Character.isLetterOrDigit(ch)) hasSpecial = true; // Спецсимволы
        }

        int score = 0;
        if (hasDigit) score++;
        if (hasLower) score++;
        if (hasUpper) score++;
        if (hasSpecial) score++;

        if (password.length() >= 12 && score >= 4) {
            return PasswordStrength.STRONG;
        }
        if (password.length() >= 8 && score >= 3) {
            return PasswordStrength.MEDIUM;
        }

        return PasswordStrength.WEAK;
    }


}
