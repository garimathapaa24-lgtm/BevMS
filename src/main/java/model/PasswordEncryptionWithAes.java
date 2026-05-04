package model;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * AES/GCM/NoPadding password encryption.
 * Prepends IV + salt to cipher text before Base64 encoding.
 */
public class PasswordEncryptionWithAes {

    private static final String ENCRYPT_ALGO  = "AES/GCM/NoPadding";
    private static final int    TAG_LENGTH_BIT = 128;
    private static final int    IV_LENGTH_BYTE = 12;
    private static final int    SALT_LENGTH    = 16;

    private static byte[] getRandomNonce(int len) {
        byte[] nonce = new byte[len];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    private static SecretKey getAESKeyFromPassword(char[] password, byte[] salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /** Encrypts plaintext password using userId as passphrase. Returns Base64 string. */
    public static String encrypt(String userId, String password) {
        try {
            byte[] salt = getRandomNonce(SALT_LENGTH);
            byte[] iv   = getRandomNonce(IV_LENGTH_BYTE);

            SecretKey key    = getAESKeyFromPassword(userId.toCharArray(), salt);
            Cipher    cipher = Cipher.getInstance(ENCRYPT_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
            byte[] cipherText = cipher.doFinal(password.getBytes("UTF-8"));

            // Prepend IV + salt + cipherText
            byte[] combined = new byte[iv.length + salt.length + cipherText.length];
            System.arraycopy(iv,         0, combined, 0,                        iv.length);
            System.arraycopy(salt,       0, combined, iv.length,                salt.length);
            System.arraycopy(cipherText, 0, combined, iv.length + salt.length,  cipherText.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
        }
    }

    /** Decrypts encrypted password back to plaintext using userId as passphrase. */
    public static String decrypt(String encryptedPassword, String userId) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedPassword);

            byte[] iv         = new byte[IV_LENGTH_BYTE];
            byte[] salt       = new byte[SALT_LENGTH];
            byte[] cipherText = new byte[combined.length - iv.length - salt.length];

            System.arraycopy(combined, 0,                        iv,         0, iv.length);
            System.arraycopy(combined, iv.length,                salt,       0, salt.length);
            System.arraycopy(combined, iv.length + salt.length,  cipherText, 0, cipherText.length);

            SecretKey key    = getAESKeyFromPassword(userId.toCharArray(), salt);
            Cipher    cipher = Cipher.getInstance(ENCRYPT_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

            return new String(cipher.doFinal(cipherText), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Decryption failed", e);
        }
    }
}