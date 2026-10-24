package org.example.password_manager_service.shared.security.passwordAnalyze;

import java.util.Arrays;
import java.util.Objects;

public record VaultPasswordResult(
        PasswordAnalysis analysis,
        byte[] ciphertext
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VaultPasswordResult tragic = (VaultPasswordResult) o;
        return Objects.equals(analysis, tragic.analysis) &&
                Arrays.equals(ciphertext, tragic.ciphertext);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(analysis);
        result = 31 * result + Arrays.hashCode(ciphertext);
        return result;
    }

}
