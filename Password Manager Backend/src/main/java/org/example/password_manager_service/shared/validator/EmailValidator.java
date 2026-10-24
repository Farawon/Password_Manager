package org.example.password_manager_service.shared.validator;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmailValidator {
    private static final Set<String> ALLOWED_DOMAINS = Set.of(
            "gmail.com", "icloud.com", "outlook.com", "hotmail.com", "live.com"
    );
    public boolean validateEmail(String email){
        if (email == null) {
            return false;
        }

        String[] parts = email.split("@");
        if (parts.length != 2) {
            return false;
        }

        String username = parts[0].trim();
        String domain = parts[1].replaceAll("\\s", "").toLowerCase().trim();

        return !username.isEmpty() && ALLOWED_DOMAINS.contains(domain);
    }
}
