package org.example.password_manager_service.entities.shared.security.encrypted;

import org.example.password_manager_service.shared.security.encrypted.DecodingSecretKey;
import org.example.password_manager_service.shared.security.encrypted.PasswordEncryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PasswordEncryptionTest {

    @Autowired
    private PasswordEncryption passwordEncryption;

    @Autowired
    private DecodingSecretKey decodingSecretKey;

    private static final String TEST_PASSWORD = "TestPassword123!@#";
    private static final String TEST_BASE64_KEY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="; // 32 bytes when decoded

    @BeforeEach
    void setUp() {
        // Set a test key for encryption/decryption
        ReflectionTestUtils.setField(decodingSecretKey, "key", TEST_BASE64_KEY);
    }

    @Test
    void testEncryptPassword() {
        byte[] encrypted = passwordEncryption.encryptedPassword(TEST_PASSWORD);

        assertNotNull(encrypted);
        assertTrue(encrypted.length > 0);
        assertNotEquals(TEST_PASSWORD.getBytes(), encrypted);
    }

    @Test
    void testDecryptPassword() {
        byte[] encrypted = passwordEncryption.encryptedPassword(TEST_PASSWORD);
        String decrypted = passwordEncryption.decryptedPassword(encrypted);

        assertEquals(TEST_PASSWORD, decrypted);
    }

    @Test
    void testEncryptDecryptCycle() {
        String originalPassword = "MySecurePassword2024!";

        byte[] encrypted = passwordEncryption.encryptedPassword(originalPassword);
        String decrypted = passwordEncryption.decryptedPassword(encrypted);

        assertEquals(originalPassword, decrypted);
    }

    @Test
    void testDifferentEncryptionsForSamePassword() {
        byte[] encrypted1 = passwordEncryption.encryptedPassword(TEST_PASSWORD);
        byte[] encrypted2 = passwordEncryption.encryptedPassword(TEST_PASSWORD);

        // GCM mode should produce different ciphertexts due to random IV
        assertNotEquals(encrypted1, encrypted2);
    }

    @Test
    void testDecryptionOfDifferentEncryptionsProducesSameResult() {
        byte[] encrypted1 = passwordEncryption.encryptedPassword(TEST_PASSWORD);
        byte[] encrypted2 = passwordEncryption.encryptedPassword(TEST_PASSWORD);

        String decrypted1 = passwordEncryption.decryptedPassword(encrypted1);
        String decrypted2 = passwordEncryption.decryptedPassword(encrypted2);

        assertEquals(decrypted1, decrypted2);
        assertEquals(TEST_PASSWORD, decrypted1);
    }

    @Test
    void testEncryptEmptyPassword() {
        String emptyPassword = "";
        byte[] encrypted = passwordEncryption.encryptedPassword(emptyPassword);
        String decrypted = passwordEncryption.decryptedPassword(encrypted);

        assertEquals(emptyPassword, decrypted);
    }

    @Test
    void testEncryptSpecialCharacters() {
        String specialPassword = "p@$$w0rd!#$%^&*()_+-=[]{}|;':\",./<>?";
        byte[] encrypted = passwordEncryption.encryptedPassword(specialPassword);
        String decrypted = passwordEncryption.decryptedPassword(encrypted);

        assertEquals(specialPassword, decrypted);
    }

    @Test
    void testDecryptInvalidData() {
        byte[] invalidData = new byte[]{1, 2, 3, 4, 5};

        assertThrows(RuntimeException.class, () -> {
            passwordEncryption.decryptedPassword(invalidData);
        });
    }

    @Test
    void testDecryptNullData() {
        assertThrows(Exception.class, () -> {
            passwordEncryption.decryptedPassword(null);
        });
    }

    @Test
    void testDecryptEmptyData() {
        byte[] emptyData = new byte[0];

        assertThrows(RuntimeException.class, () -> {
            passwordEncryption.decryptedPassword(emptyData);
        });
    }

    @Test
    void testEncryptVeryLongPassword() {
        String longPassword = "a".repeat(1000) + "!@#123A";
        byte[] encrypted = passwordEncryption.encryptedPassword(longPassword);
        String decrypted = passwordEncryption.decryptedPassword(encrypted);

        assertEquals(longPassword, decrypted);
    }

    @Test
    void testDecodingSecretKey() {
        byte[] decodedKey = decodingSecretKey.decodeBase64();

        assertNotNull(decodedKey);
        assertEquals(32, decodedKey.length); // AES-256 requires 32 bytes
    }
}
