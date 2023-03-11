package com.bitvault.algos;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

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

//    @Test
    void stressTest() {
        final String masterPass = "masterPass";
        final String salt = "thisIsSalty";
        final String value = "encrypencryptThisencryptThisencryptThisencryptThisencryptThisencryptThisencryptThistThis";

        try {
            final SecretKey secretKey = AES.secretKey(masterPass.toCharArray(), salt.getBytes());

            List<String> encList = new ArrayList<>();

            for (int i = 0; i < 10000; i++) {
                final byte[] ivBytes = AES.generateIv();
                final byte[] newVal = (value + i).getBytes();

                final byte[] encrypted = AES.encryptIvIncluded(newVal, secretKey, ivBytes);

                String s = Base64.getEncoder().encodeToString(encrypted);
                encList.add(s);
            }

            LocalDateTime start = LocalDateTime.now();

            for (String encoded : encList) {
                byte[] encrypted = Base64.getDecoder().decode(encoded);
                final byte[] decrypted = AES.decryptIvIncluded(encrypted, secretKey);
            }

            LocalDateTime end = LocalDateTime.now();

            Duration duration = Duration.between(start, end);

            System.out.println(duration.toMillis());

        } catch (Exception ex) {
            fail(ex);
        }


    }
}