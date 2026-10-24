package org.example.password_manager_service.entities.shared.validator;

import org.example.password_manager_service.shared.validator.PasswordLevel;
import org.example.password_manager_service.shared.validator.model.PasswordStrength;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class PasswordLevelTest {
    private final PasswordLevel passwordLevel = new PasswordLevel();

    @ParameterizedTest()
    @CsvSource({
            "'test', WEAK",
            "'test321', WEAK",
            "'l31@Lasd', MEDIUM",
            "'tEs@t', WEAK",
            "'linee2', WEAK",
            "'|213ASxczASDe2', STRONG",
            "'testtesttesttest', WEAK",
            "'TestTest231@', STRONG",
            "'dasdsaASzX21', MEDIUM"
    })
    void passwordStrengthTest(String rawPass, PasswordStrength expected){
        PasswordStrength passwordStrength =  passwordLevel.isPasswordValid(rawPass);
        assertEquals(expected, passwordStrength);
    }
}
