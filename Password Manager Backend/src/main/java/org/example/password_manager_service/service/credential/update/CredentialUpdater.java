package org.example.password_manager_service.service.credential.update;


import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.credential.CredentialCategories;

import java.util.UUID;

public interface CredentialUpdater {
    Credential updateName(UUID id, String name);
    Credential updateWebsite( UUID id, String website);
    Credential updateEmail(UUID id, String email);
    Credential updateCategories(UUID id, CredentialCategories categories);
    Credential updatePassword(UUID id, String password);
}
