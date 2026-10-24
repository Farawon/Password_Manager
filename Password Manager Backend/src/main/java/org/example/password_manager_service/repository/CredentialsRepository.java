package org.example.password_manager_service.repository;

import org.example.password_manager_service.entities.credential.Credential;
import org.example.password_manager_service.entities.credential.CredentialCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CredentialsRepository extends JpaRepository<Credential, UUID> {
    List<Credential> findByCredentialCategory(CredentialCategories credentialCategories);
    List<Credential> findAllByUser_Email(String email);
    boolean existsByIdAndUser_Email(UUID id, String email);
}
