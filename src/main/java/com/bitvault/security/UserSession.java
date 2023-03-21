package com.bitvault.security;

import java.util.Arrays;

public class UserSession {


    private final String location;
    private final char[] username;

    private final EncryptionProvider encryptionProvider;

    public static UserSession newAesSession(String filepath, String username, String password) {


        EncryptionProvider provider = new AesEncryptionProvider(password.toCharArray());

        return new UserSession(filepath, username.toCharArray(), provider);
    }

    private UserSession(String location, char[] username, EncryptionProvider encryptionProvider) {
        this.location = location;
        this.username = username;
        this.encryptionProvider = encryptionProvider;
    }

    public void discard() {
        Arrays.fill(this.username, (char) 0);
        this.encryptionProvider.destroy();
    }

    public String getLocation() {
        return location;
    }

    public String getUsername() {
        return new String(username);
    }


    public EncryptionProvider getEncryptionProvider() {
        return encryptionProvider;
    }
}
