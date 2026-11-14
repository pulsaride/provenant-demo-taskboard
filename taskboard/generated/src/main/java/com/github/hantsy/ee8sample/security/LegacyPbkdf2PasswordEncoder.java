package com.github.hantsy.ee8sample.security;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Verifies passwords stored by the legacy Pbkdf2PasswordHash:
 * {@code <algorithm>:<iterations>:<base64 salt>:<base64 hash>}. Every parameter
 * is read from the stored string, so existing hashes keep working. Anything that
 * does not parse as that format does not match; a stored hash that cannot be
 * processed is logged (exception class only) so it is told apart from a wrong password.
 */
public class LegacyPbkdf2PasswordEncoder implements PasswordEncoder {

    private static final Logger LOG = LoggerFactory.getLogger(LegacyPbkdf2PasswordEncoder.class);
    private static final Set<String> ALGORITHMS = Set.of(
            "PBKDF2WithHmacSHA1", "PBKDF2WithHmacSHA224", "PBKDF2WithHmacSHA256",
            "PBKDF2WithHmacSHA384", "PBKDF2WithHmacSHA512");
    private static final int PARTS = 4;
    /** Defaults of the container hash the legacy uses (Pbkdf2PasswordHash without configuration). */
    private static final String DEFAULT_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int DEFAULT_ITERATIONS = 2048;
    private static final int DEFAULT_SALT_BYTES = 32;
    private static final int DEFAULT_KEY_BYTES = 32;
    /** Bounds on a stored hash; anything outside is not a hash this application stored or accepts. */
    private static final int MAX_ITERATIONS = 1_000_000;
    private static final int MIN_SALT_BYTES = 8;
    private static final int MAX_SALT_BYTES = 64;
    private static final int MIN_KEY_BYTES = 16;
    private static final int MAX_KEY_BYTES = 64;
    private final SecureRandom random = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        // Spring calls this for its timing-attack protection; the output is the
        // legacy format with the container defaults, so it verifies like a stored hash.
        byte[] salt = new byte[DEFAULT_SALT_BYTES];
        random.nextBytes(salt);
        try {
            byte[] key = derive(DEFAULT_ALGORITHM, rawPassword.toString(), salt, DEFAULT_ITERATIONS, DEFAULT_KEY_BYTES);
            return DEFAULT_ALGORITHM + ":" + DEFAULT_ITERATIONS + ":"
                    + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(key);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 is not available", e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        String[] parts = storedHash.split(":", -1);
        if (parts.length != PARTS || !ALGORITHMS.contains(parts[0])) {
            return false;
        }
        try {
            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getDecoder().decode(parts[2].getBytes(StandardCharsets.UTF_8));
            byte[] expected = Base64.getDecoder().decode(parts[3].getBytes(StandardCharsets.UTF_8));
            if (iterations <= 0 || iterations > MAX_ITERATIONS
                    || salt.length < MIN_SALT_BYTES || salt.length > MAX_SALT_BYTES
                    || expected.length < MIN_KEY_BYTES || expected.length > MAX_KEY_BYTES) {
                return false;
            }
            byte[] actual = derive(parts[0], rawPassword.toString(), salt, iterations, expected.length);
            return MessageDigest.isEqual(expected, actual);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            // Never the password, the stored hash or the exception message (it can echo a hash field).
            LOG.warn("Stored password hash could not be processed ({}); treated as no match", e.getClass().getSimpleName());
            return false;
        }
    }

    private static byte[] derive(String algorithm, String password, byte[] salt, int iterations, int keyBytes)
            throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, keyBytes * 8);
        return SecretKeyFactory.getInstance(algorithm).generateSecret(spec).getEncoded();
    }
}
