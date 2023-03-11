package com.bitvault.security;

public interface EncryptionProvider {

    String encrypt(String value);

    String decrypt(String value);

}
