package org.example.password_manager_service.shared.security.JWT;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(@NonNull String email){
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new CustomUserDetails(user);
    }
}
