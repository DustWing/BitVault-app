package com.bitvault.server.cache;

import com.bitvault.algos.AES;
import com.bitvault.server.dto.SecureItemRqDto.LocalPasswordDto;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

public final class ImportCache {

    private final ConcurrentLinkedQueue<LocalPasswordDto> cache;
    private Consumer<LocalPasswordDto> onAddPassword;

    private final SecretKey secretKey;
    private final byte[] IV;


    /**
     * @param onAddPassword consumer when adding new password
     * @return new ImportCache with random secret key
     */
    public static ImportCache createDefault(Consumer<LocalPasswordDto> onAddPassword) {

        SecretKey secretKey;
        try {
            secretKey = AES.randomSecretKey();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        byte[] iv = AES.generateIv();

        return new ImportCache(new ConcurrentLinkedQueue<>(), secretKey, iv, onAddPassword);
    }

    public ImportCache(ConcurrentLinkedQueue<LocalPasswordDto> cache, SecretKey secretKey, byte[] iv, Consumer<LocalPasswordDto> onAddPassword) {
        this.cache = cache;
        this.secretKey = secretKey;
        this.IV = iv;
        this.onAddPassword = onAddPassword;
    }


    public List<LocalPasswordDto> getCache() {
        return cache.stream().toList();
    }

    public void add(LocalPasswordDto password) {
        cache.add(password);
        onAddPassword.accept(password);
    }

    public void remove(LocalPasswordDto password) {
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
