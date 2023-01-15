package com.bitvault.server.http;

import com.bitvault.algos.AES;
import com.bitvault.server.dto.KeyDto;
import com.bitvault.server.dto.SecureItemRqDto;
import com.bitvault.util.Json;
import com.bitvault.util.Result;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class TestServer {

    public static void main(String[] args) throws Exception {

        HttpClient httpClient = HttpClient
                .newBuilder()
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://127.0.0.1:8080/key"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient
                .send(request, HttpResponse.BodyHandlers.ofString());


        if (response.statusCode() != 200) {
            throw new RuntimeException(response.body());
        }

        Result<KeyDto> keyDtoResult = Json.deserialize(response.body(), KeyDto.class);

        if (keyDtoResult.isFail()) {
            throw keyDtoResult.getError();
        }
        KeyDto keyDto = keyDtoResult.get();
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
            throw serialize.getError();
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
            throw body.getError();
        }


        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI("http://127.0.0.1:8080/shareSecureItem"))
                .POST(HttpRequest.BodyPublishers.ofString(body.get()))
                .build();

        httpClient
                .send(request2, HttpResponse.BodyHandlers.ofString());
    }
}
