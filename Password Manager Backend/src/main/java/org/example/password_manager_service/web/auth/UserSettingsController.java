package org.example.password_manager_service.web.auth;

import org.example.password_manager_service.service.user_settings.GetUserSettingsDto;
import org.example.password_manager_service.service.user_settings.UserSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserSettingsController {
    private UserSettingsService userSettingsService;

    @Autowired
    public UserSettingsController(UserSettingsService userSettingsService) {
        this.userSettingsService = userSettingsService;
    }
    @GetMapping
    public GetUserSettingsDto getUserSettings(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return  userSettingsService.getUser(username);
    }
}
