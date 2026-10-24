package org.example.password_manager_service.service.credential;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings("java:S2259")
public class CurrentUserService {
    public String getCurrentUserEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
