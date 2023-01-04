package com.bitvault.server.endpoints;

import com.bitvault.algos.AES;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.model.KeyDto;
import com.bitvault.server.model.ResultRsDto;
import com.bitvault.server.model.SecureItemRqDto;
import com.bitvault.util.Json;
import com.bitvault.util.Result;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class SecureItemControllerTest {

    private static SecureItemController secureItemController;

    @BeforeAll
    public static void init() {

        ImportCache importCache = ImportCache.create();

        secureItemController = new SecureItemController(importCache);
    }

    @Test
    public void get() {

        Result<KeyDto> keyDtoResult = secureItemController.get();
        if (keyDtoResult.isFail()) {
            fail(keyDtoResult.getError());
        }

        KeyDto keyDto = keyDtoResult.get();
        System.out.println(keyDto);

    }

    @Test
    public void post() {
        Result<KeyDto> keyDtoResult = secureItemController.get();
        if (keyDtoResult.isFail()) {
            fail(keyDtoResult.getError());
        }

        KeyDto keyDto = keyDtoResult.get();
        System.out.println(keyDto);


        String encoded = keyDto.key();
        byte[] decode = Base64.getDecoder().decode(encoded);
        byte[] iv = Arrays.copyOf(decode, 12);
        byte[] key = Arrays.copyOfRange(decode, 12, decode.length);

        SecretKey secretKey;
        try {
            secretKey = AES.secretKey(key);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        SecureItemRqDto.LocalPasswordDto localPasswordDto = new SecureItemRqDto.LocalPasswordDto(
                "Id123",
                "MyUserName",
                "MyPassword",
                new SecureItemRqDto.DomainDetailsDto("Domain", "Domain", "Domain.com", "Domain"),
                "Detail",
                "Description",
                true,
                false,
                "",
                "",
                "",
                "",
                "",
                "",
                SecureItemRqDto.SecureItemSharedType.PRIVATE
        );

        Result<String> serialize = Json.serialize(localPasswordDto);

        if (serialize.isFail()) {
            fail(serialize.getError());
            return;
        }

        String s = serialize.get();

        byte[] encrypt;
        try {
            encrypt = AES.encrypt(s.getBytes(), secretKey, iv);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

        String encryptedPayload = Base64.getEncoder().encodeToString(encrypt);

        SecureItemRqDto secureItemRqDto = new SecureItemRqDto(
                "PASSWORD",
                encryptedPayload
        );

        Result<String> body = Json.serialize(secureItemRqDto);
        if (body.isFail()) {
            fail(body.getError());
            return;
        }

        Result<ResultRsDto> post = secureItemController.post(body.get());
        if (post.isFail()) {
            fail(post.getError());
            return;
        }

        System.out.println(post.get());
    }
}