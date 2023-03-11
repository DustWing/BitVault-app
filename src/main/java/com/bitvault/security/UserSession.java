package com.bitvault.security;

import com.bitvault.algos.AES;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

public class UserSession implements EncryptionProvider{


    private final String location;
    private final char[] username;
    private final char[] password;

    private final SecretKey secretKey;

    public static UserSession newSession(String filepath, String username, String password) {
        return new UserSession(filepath, username.toCharArray(), password.toCharArray());
    }

    private UserSession(String location, char[] username, char[] password) {
        this.location = location;
        this.username = username;
        this.password = password;
        this.secretKey = getSecretKey();
    }

    public void discard() {
        Arrays.fill(this.password, (char) 0);
        Arrays.fill(this.username, (char) 0);
        try {
            this.secretKey.destroy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return new String(username);
    }

    private SecretKey getSecretKey() {
        try {
            return AES.secretKey(this.password, "bitVault".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String value) {
        final byte[] ivBytes = AES.generateIv();
        final byte[] valueBytes = value.getBytes();
        final byte[] encrypted;
        try {
            encrypted = AES.encryptIvIncluded(valueBytes, this.secretKey, ivBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decrypt(String value) {
        byte[] encrypted = Base64.getDecoder().decode(value);
        try {
            final byte[] decrypted = AES.decryptIvIncluded(encrypted, this.secretKey);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
