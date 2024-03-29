package com.bitvault.algos;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

public class AES {

    private static final String ALGORITHM = "AES";
    private static final String FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATION_COUNT = 310000;
    private static final int KEY_SIZE = 256;
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final String ENCRYPTION = "AES/GCM/NoPadding";

    public static SecretKey randomSecretKey() throws NoSuchAlgorithmException {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(KEY_SIZE);
        return keyGenerator.generateKey();
    }


    public static byte[] generateIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static SecretKey secretKey(final char[] from, final byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        final SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_ALGORITHM);
        final KeySpec spec = new PBEKeySpec(from, salt, ITERATION_COUNT, KEY_SIZE);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), ALGORITHM);
    }

    public static SecretKey secretKey(final byte[] bytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(bytes, ALGORITHM);
    }

    public static byte[] encrypt(final byte[] value, final SecretKey key, final byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Get Cipher Instance
        final Cipher cipher = Cipher.getInstance(ENCRYPTION);

        // Create SecretKeySpec
        final SecretKey keySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);

        // Create GCMParameterSpec
        final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Initialize Cipher for ENCRYPT_MODE
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Encryption
        return cipher.doFinal(value);
    }

    public static byte[] encryptIvIncluded(final byte[] value, final SecretKey key, final byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        byte[] encrypted = encrypt(value, key, iv);
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return combined;
    }

    public static byte[] decrypt(final byte[] value, final SecretKey key, final byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // Get Cipher Instance
        final Cipher cipher = Cipher.getInstance(ENCRYPTION);

        // Create SecretKeySpec
        final SecretKey keySpec = new SecretKeySpec(key.getEncoded(), ALGORITHM);

        // Create GCMParameterSpec
        final GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

        // Perform Decryption
        return cipher.doFinal(value);
    }


    public static byte[] decryptIvIncluded(final byte[] value, final SecretKey key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] iv = Arrays.copyOf(value, 12);
        byte[] encrypted = Arrays.copyOfRange(value, 12, value.length);

        return decrypt(encrypted, key, iv);
    }

}
