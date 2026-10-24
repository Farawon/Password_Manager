package org.example.password_manager_service.shared.security.encrypted;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class PasswordEncryption {
    private final DecodingSecretKey decodingSecretKey;

    public PasswordEncryption(DecodingSecretKey decodingSecretKey){
        this.decodingSecretKey = decodingSecretKey;
    }
    public byte[] encryptedPassword(String text){
        try {

            SecretKeySpec key = new SecretKeySpec( decodingSecretKey.decodeBase64(), "AES");
            // encryption
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key); // tag save auto
            byte[] encryption = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)); // encrypted password
            byte[] iv = cipher.getIV(); // IV in db save in String
            byte[] result = new byte[ iv.length + encryption.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encryption, 0, result, iv.length, encryption.length);
            return result;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public String decryptedPassword(byte[] encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(decodingSecretKey.decodeBase64(), "AES");
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

            int ivLength = 12;
            byte[] iv = new byte[ivLength];
            System.arraycopy(encryptedData, 0, iv, 0, ivLength);

            int ciphertextLength = encryptedData.length - ivLength;
            byte[] ciphertext = new byte[ciphertextLength];
            System.arraycopy(encryptedData, ivLength, ciphertext, 0, ciphertextLength);

            GCMParameterSpec spec = new GCMParameterSpec(128, iv);

            cipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte[] decryptedBytes = cipher.doFinal(ciphertext);

            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error in decrypted password", e);
        }
    }
    public boolean matchPass(String rawPass, byte[] pass){
        String decodePass = decryptedPassword(pass);
        return MessageDigest.isEqual(rawPass.getBytes(StandardCharsets.UTF_8),
                decodePass.getBytes(StandardCharsets.UTF_8));
    }

}