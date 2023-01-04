package com.bitvault.server.cache;

import com.bitvault.algos.AES;
import com.bitvault.ui.model.Password;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ImportCache {

    private final ConcurrentLinkedQueue<Password> cache;


    private final SecretKey secretKey;
    private final byte[] IV;


    public static ImportCache create() {

        SecretKey secretKey;
        try {
            secretKey = AES.randomSecretKey();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] iv = AES.generateIv();

        return new ImportCache(new ConcurrentLinkedQueue<>(), secretKey, iv);
    }

    private ImportCache(ConcurrentLinkedQueue<Password> cache, SecretKey secretKey, byte[] iv) {
        this.cache = cache;
        this.secretKey = secretKey;
        this.IV = iv;
    }

    public List<Password> getCache() {
        return cache.stream().toList();
    }

    public void add(Password password) {
        cache.add(password);
    }

    public void remove(Password password) {
        cache.remove(password);
    }

    public void clear() {
        cache.clear();
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public byte[] getIV() {
        return IV;
    }

}
