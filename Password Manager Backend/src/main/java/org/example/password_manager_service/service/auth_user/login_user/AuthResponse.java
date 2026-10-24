package org.example.password_manager_service.service.auth_user.login_user;

public class AuthResponse{
    private final String access;


    public AuthResponse(String access) {
        this.access = access;
    }

    public String getAccessToken(){
        return access;
    }

}
