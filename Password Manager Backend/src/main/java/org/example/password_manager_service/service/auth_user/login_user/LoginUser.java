package org.example.password_manager_service.service.auth_user.login_user;


public interface LoginUser {
    AuthResponse execute(LoginUserRequest loginUserRequest);
}
