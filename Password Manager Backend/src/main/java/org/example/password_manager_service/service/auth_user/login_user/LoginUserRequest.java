package org.example.password_manager_service.service.auth_user.login_user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserRequest (

    @NotBlank(message = "Email can't be empty")
    @Email(message = "This is not supported email")
    String email,


    @NotBlank(message = "Password can't be empty")
    @Size(min = 12, message = "Password must have minimum 12 characters")
    String rawPassword
){}
