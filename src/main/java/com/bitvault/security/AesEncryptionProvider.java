package com.bitvault.security;

import com.bitvault.algos.AES;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;

public class AesEncryptionProvider implements EncryptionProvider {


    private final SecretKey secretKey;

    public AesEncryptionProvider(char[] password) {

        try {
            this.secretKey = AES.secretKey(password, "bitVault".getBytes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            Arrays.fill(password, '0');
        }

    }


    @Override
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

    @Override
    public String decrypt(String value) {
        byte[] encrypted = Base64.getDecoder().decode(value);
        try {
            final byte[] decrypted = AES.decryptIvIncluded(encrypted, this.secretKey);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        //cannot destroy secret key it throws exception
//        try {
//            this.secretKey.destroy();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

}
