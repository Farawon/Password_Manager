package org.example.password_manager_service.web.credential;


import jakarta.validation.Valid;
import org.example.password_manager_service.service.credential.create.CreateCredentialRequest;
import org.example.password_manager_service.service.credential.create.CredentialCreatorUseCase;
import org.example.password_manager_service.service.credential.create.CredentialResponse;
import org.example.password_manager_service.service.credential.delete.CredentialDeleteUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialProviderUseCase;
import org.example.password_manager_service.service.credential.provider.CredentialResponseDto;
import org.example.password_manager_service.service.credential.update.CredentialUpdateRequest;
import org.example.password_manager_service.service.credential.update.CredentialUpdateResponse;
import org.example.password_manager_service.service.credential.update.UpdaterFilter;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/credentials")
public class CredentialController {
    private final CredentialCreatorUseCase credentialCreatorUseCase;
    private final CredentialDeleteUseCase credentialDeleteUseCase;
    private final CredentialProviderUseCase  credentialProviderUseCase;
    private final UpdaterFilter updaterFilter;

    public CredentialController(CredentialCreatorUseCase credentialCreatorUseCase, CredentialDeleteUseCase credentialDeleteUseCase, CredentialProviderUseCase credentialProviderUseCase, UpdaterFilter updaterFilter) {
        this.credentialCreatorUseCase = credentialCreatorUseCase;
        this.credentialDeleteUseCase = credentialDeleteUseCase;
        this.credentialProviderUseCase = credentialProviderUseCase;
        this.updaterFilter = updaterFilter;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CredentialResponse createCredential(@Valid @RequestBody CreateCredentialRequest createCredentialRequest){
        return credentialCreatorUseCase.createCredential(createCredentialRequest);
    }

    @DeleteMapping("/{credentialId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@credentialSecurity.isOwner(#id, authentication)")
    public void deleteCredential(@PathVariable("credentialId")  UUID id){
        credentialDeleteUseCase.removeCredential(id);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@credentialSecurity.isOwner(#id, authentication)")
    public CredentialUpdateResponse updateCredential(@Valid @RequestBody CredentialUpdateRequest credentialUpdateRequest, @AuthenticationPrincipal UserDetails userDetails){
            return updaterFilter.filter(credentialUpdateRequest);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@credentialSecurity.isOwner(#id, authentication)")
    public CredentialResponseDto getCredential(@PathVariable UUID id){
        return credentialProviderUseCase.credentialProvider(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CredentialResponseDto> getCredentials(Authentication authentication){
        String userEmail = authentication.getName();
        return credentialProviderUseCase.credentialsProvider(userEmail);
    }

    @GetMapping("/password/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("@credentialSecurity.isOwner(#id, authentication)")
    public String  getPassword(@PathVariable UUID id){
        return credentialProviderUseCase.getCredentialPassword(id);
    }
}
