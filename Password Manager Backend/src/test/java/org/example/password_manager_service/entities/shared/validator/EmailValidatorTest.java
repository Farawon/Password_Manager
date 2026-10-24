package org.example.password_manager_service.entities.shared.validator;

import org.example.password_manager_service.shared.validator.EmailValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmailValidatorTest {
    private final EmailValidator emailValidator = new EmailValidator();

    @ParameterizedTest
    @CsvSource({
        "'ad@gmail.com', true",
        "'gmail@gmail.com', true",
        "'user31E@gmail.com', true",
        "'user@icloud.com', true",
        "'user@outlook.com', true",
        "'user@hotmail.com', true",
        "'user@live.com', true",
        "'sdadsasadds@gmail.com', true",
        "'@gmail.com', false",
        "'test gmail.com', false"
    })
    void emailTest(String email, boolean expected){
        boolean emailValid = emailValidator.validateEmail(email);
        assertEquals(expected, emailValid);
    }

}
