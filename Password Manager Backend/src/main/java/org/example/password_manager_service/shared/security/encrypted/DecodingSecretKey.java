package org.example.password_manager_service.shared.security.encrypted;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class DecodingSecretKey {

    @Value("${encryption.key}")
    private String key;

    public byte[] decodeBase64(){
        return Base64.getDecoder().decode(key);
    }
}
