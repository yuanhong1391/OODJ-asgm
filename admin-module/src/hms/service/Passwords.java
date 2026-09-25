package hms.service;

import java.security.*;
import java.util.*;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

final class Passwords {
    private static final int ITERATIONS = 120000;
    private Passwords() { }
    static String hash(String password) {
        if (password.length() < 8) throw new IllegalArgumentException("Password must contain at least 8 characters.");
        byte[] salt = new byte[16]; new SecureRandom().nextBytes(salt);
        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
    }
    static boolean matches(String password, String stored) {
        try {
            String[] parts = stored.split(":");
            int rounds = Integer.parseInt(parts[0]);
            if (rounds < 10000 || rounds > 1000000) return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(parts[2]), derive(password, Base64.getDecoder().decode(parts[1]), rounds));
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ex) { return false; }
    }
    private static byte[] derive(String password, byte[] salt, int rounds) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, rounds, 256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch (GeneralSecurityException ex) { throw new IllegalStateException("Password hashing is unavailable.", ex); }
        finally { spec.clearPassword(); }
    }
}
