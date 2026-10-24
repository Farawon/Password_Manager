package org.example.password_manager_service.shared.utility;


import org.springframework.stereotype.Component;

import java.util.HexFormat;
@Component
public class HexConverter {
    public String hexFormat(byte[] encryptingPass){
        return HexFormat.of().withUpperCase().formatHex(encryptingPass);
    }
}
