package com.bitvault.algos;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    @Test
    void encrypt() {

        final String value = "encryptThis";
        final String masterPass = "masterPass";
        final String salt = "thisIsSalty";

        try {

            final SecretKey secretKey = AES.secretKey(masterPass.toCharArray(), salt.getBytes());
            final byte[] iv = AES.generateIv();
            final byte[] encrypted = AES.encrypt(value.getBytes(), secretKey, iv);
            final byte[] decrypted = AES.decrypt(encrypted, secretKey, iv);

            assertArrayEquals(value.getBytes(), decrypted);

        } catch (Exception ex) {
            fail(ex);
        }

    }
}