package org.example.password_manager_service.service.auth_user.register_user;

import jakarta.validation.constraints.*;

public record RegisterUserRequest(

        @NotBlank(message = "This field can't be empty")
        @Size(min = 4, max = 25, message = "Full name must be between 4 and 25 characters")
        String firstName,

        @NotBlank(message = "This field can't be empty")
        @Size(min = 4, max = 25, message = "Full name must be between 4 and 25 characters")
        String lastName,

        @NotBlank(message = "Email can't be empty")
        @Email(message = "This is not supported email")
        String email,

        @NotBlank(message = "Password can't be empty")
        @Size(min = 12, message = "Password must have minimum 12 characters")
        String rawPassword,

        @NotBlank(message = "Confirm password can't be empty")
        String confirmPassword,

        @NotNull(message = "You must accept the terms and conditions.")
        @AssertTrue(message = "You must accept the terms and conditions.")
        Boolean isTermsAccepted

){
    @AssertTrue(message = "Passwords do not match")
    public boolean isPasswordsMatching() {
        return rawPassword != null && rawPassword.equals(confirmPassword);
    }
}

