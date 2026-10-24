package org.example.password_manager_service.service.auth_user.register_user;

import org.example.password_manager_service.shared.exceptions.LeakedPasswordException;
import org.example.password_manager_service.shared.security.passwordAnalyze.AuthPasswordResult;
import org.example.password_manager_service.shared.security.PasswordProcessor;
import org.example.password_manager_service.service.cards_service.stripe.customer.StripeCustomerGateway;
import org.example.password_manager_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserUseCase implements RegisterUser {
    private final UserDataValidator userDataValidator;
    private final UserRepository userRepository;
    private final PasswordProcessor passwordProcessor;
    private final StripeCustomerGateway stripeCustomerGateway;
    private final SaveUserInTransaction saveUserInTransaction;
    public RegisterUserUseCase(UserDataValidator userDataValidator,
                               UserRepository userRepository,
                               PasswordProcessor passwordProcessor, StripeCustomerGateway stripeCustomerGateway, SaveUserInTransaction saveUserInTransaction){
      this.userDataValidator = userDataValidator;
      this.userRepository = userRepository;
      this.passwordProcessor = passwordProcessor;
      this.stripeCustomerGateway = stripeCustomerGateway;
      this.saveUserInTransaction = saveUserInTransaction;
    }

    @Override
    public void execute(RegisterUserRequest request){
        if(!userDataValidator.isValid(request.email(), request.rawPassword())){throw new IllegalArgumentException("Invalid email or password");}

        if(userRepository.existsByEmail(request.email())){
            throw new IllegalArgumentException("We already have this email");
        }

        AuthPasswordResult passwordAnalysis = passwordProcessor.processAndPasswordHash(request.rawPassword());

        if(passwordAnalysis.analysis().isLeaked()){throw  new LeakedPasswordException("Your password is in the leaked databases");}
        String stripeCustomerId = stripeCustomerGateway.createCustomerId(request.email());
        saveUserInTransaction.saveUserInTransaction(request, passwordAnalysis.phcHash(), stripeCustomerId);
    }
}
