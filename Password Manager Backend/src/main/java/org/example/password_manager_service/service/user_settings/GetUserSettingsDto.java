package org.example.password_manager_service.service.user_settings;

import org.example.password_manager_service.entities.user.UserSubscription;
import java.util.UUID;

public record GetUserSettingsDto(
   UUID id,
   String email,
   String firstName,
   String lastName,
   UserSubscription plan
) {
}
