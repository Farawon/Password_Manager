package org.example.password_manager_service.service.user_settings;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {
    private UserRepository userRepository;

    public UserSettingsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public GetUserSettingsDto getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new GetUserSettingsDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getUserSubscription()
        );
    }
}
