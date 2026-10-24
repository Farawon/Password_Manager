package org.example.password_manager_service.entities.shared.security.passwordAnalyze;

import org.example.password_manager_service.shared.security.CheckPasswordLeakUseCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class CheckPasswordLeakUseCaseTest {
    @Autowired
    private CheckPasswordLeakUseCase checkPasswordLeakUseCase;
    @ParameterizedTest
    @CsvSource({
        "'test', true",
        "'test21', true",
        "'test@12', true",
        "'Test', true",
        "'Teeest23@123x', false"
    })
    void testCheckPasswordLeak(String password, boolean expected){
        assertEquals(expected, checkPasswordLeakUseCase.isPasswordLeaked(password));
    }
}
