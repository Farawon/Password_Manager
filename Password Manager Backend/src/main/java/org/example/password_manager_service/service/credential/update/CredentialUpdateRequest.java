package org.example.password_manager_service.service.credential.update;

import org.example.password_manager_service.entities.credential.CredentialCategories;

import java.util.UUID;

public record CredentialUpdateRequest(
    UUID id,
    String name,
    String website,
    String email,
    String password,
    CredentialCategories categories
){

}
