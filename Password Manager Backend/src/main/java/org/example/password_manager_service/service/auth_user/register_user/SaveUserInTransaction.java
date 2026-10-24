package org.example.password_manager_service.service.auth_user.register_user;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SaveUserInTransaction {
    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public SaveUserInTransaction(UserRepository userRepository, UserFactory userFactory) {
        this.userRepository = userRepository;
        this.userFactory = userFactory;
    }

    public void saveUserInTransaction(RegisterUserRequest request, String hash, String stripeId) {
        User user = userFactory.create(request, hash, stripeId);
        userRepository.save(user);
    }
}
