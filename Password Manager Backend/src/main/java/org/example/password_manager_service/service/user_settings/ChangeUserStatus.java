package org.example.password_manager_service.service.user_settings;

import org.example.password_manager_service.entities.user.User;
import org.example.password_manager_service.entities.user.UserSubscription;
import org.example.password_manager_service.repository.UserRepository;
import org.example.password_manager_service.shared.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ChangeUserStatus {
    private final UserRepository userRepository;

    public ChangeUserStatus(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void execute(Long subscriptionPrice, String customer){
        User user = userRepository.findByStripeCustomerId(customer).orElseThrow(UserNotFoundException::new);
        if(subscriptionPrice == 900){
            user.setUserSubscription(UserSubscription.PRO);
        }else if(subscriptionPrice == 2900){
            user.setUserSubscription(UserSubscription.BUSINESS);
        }
        userRepository.save(user);
    }
}
