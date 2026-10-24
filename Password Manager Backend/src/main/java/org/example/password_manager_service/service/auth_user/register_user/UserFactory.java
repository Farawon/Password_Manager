package org.example.password_manager_service.service.auth_user.register_user;


import org.example.password_manager_service.entities.user.User;
import org.springframework.stereotype.Component;


@Component
public class UserFactory {
    public User create(RegisterUserRequest request, String passwordHash, String stripeCustomerId) {
        User user = new User();
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setStripeCustomerId(stripeCustomerId);
        user.setHashedPassword(passwordHash);
        user.setIsTermsAccepted(request.isTermsAccepted());

        return user;
    }
}
