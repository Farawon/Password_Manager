package org.example.password_manager_service.service.credential.create;

import jakarta.validation.constraints.Email;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.hibernate.validator.constraints.URL;

public record CreateCredentialRequest (

        @NotBlank(message = "This field can't be empty")
        @Size(min = 4, max = 100)
        String name,


        @NotBlank(message = "This field can't be empty")
        @Size(min = 5, max = 64)
        @URL
        String website,


        @NotBlank(message = "This field can't be empty")
        @Email(message = "This is not supported email")
        String email,

        @NotBlank(message = "This field can't be empty")
        @Size(min = 8, max = 50)
        String rawPassword,

        @NotNull(message = "Category must be specified")
        CredentialCategories credentialCategories
){
}
