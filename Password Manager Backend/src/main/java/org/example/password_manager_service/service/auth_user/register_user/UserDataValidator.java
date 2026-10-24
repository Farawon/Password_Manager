package org.example.password_manager_service.service.auth_user.register_user;

import org.example.password_manager_service.shared.validator.EmailValidator;
import org.example.password_manager_service.shared.validator.PasswordLevel;
import org.example.password_manager_service.shared.validator.model.PasswordStrength;
import org.springframework.stereotype.Component;


@Component
public class UserDataValidator {
    private final PasswordLevel passwordLevel;
    private final EmailValidator emailValidator;

    public UserDataValidator(PasswordLevel passwordLevel, EmailValidator emailValidator){
        this.passwordLevel = passwordLevel;
        this.emailValidator = emailValidator;
    }

    public boolean isValid(String email, String pass){
        boolean isEmailValid = emailValidator.validateEmail(email);
        PasswordStrength isPasswordValid = passwordLevel.isPasswordValid(pass);
        return isEmailValid && isPasswordValid == PasswordStrength.STRONG;
    }
}
