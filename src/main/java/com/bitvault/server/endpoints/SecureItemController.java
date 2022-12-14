package com.bitvault.server.endpoints;

import com.bitvault.algos.AES;
import com.bitvault.enums.Action;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.model.KeyDto;
import com.bitvault.server.model.ResultRsDto;
import com.bitvault.server.model.SecureItemRqDto;
import com.bitvault.ui.model.Category;
import com.bitvault.ui.model.Password;
import com.bitvault.ui.model.SecureDetails;
import com.bitvault.util.Json;
import com.bitvault.util.Result;

import java.time.LocalDateTime;
import java.util.Base64;

public class SecureItemController {

    private final ImportCache importCache;


    public SecureItemController(ImportCache importCache) {
        this.importCache = importCache;
    }


    public Result<KeyDto> get() {


        byte[] ivBytes = importCache.getIV();
        byte[] keyBytes = importCache.getSecretKey().getEncoded();

        byte[] combined = new byte[ivBytes.length + keyBytes.length];
        System.arraycopy(ivBytes, 0, combined, 0, ivBytes.length);
        System.arraycopy(keyBytes, 0, combined, ivBytes.length, keyBytes.length);

        String key = Base64.getEncoder().encodeToString(combined);

        String algorithm = importCache.getSecretKey().getAlgorithm();

        return Result.ok(
                new KeyDto(key, algorithm)
        );
    }


    private Result<String> decryptPayload(String encryptedPayload) {
        byte[] decoded = Base64.getDecoder().decode(encryptedPayload);

        try {
            byte[] decryptBytes = AES.decrypt(
                    decoded,
                    importCache.getSecretKey(),
                    importCache.getIV()
            );

            return Result.ok(new String(decryptBytes));
        } catch (Exception e) {
            return Result.error(e);
        }
    }

    private Result<Boolean> addToPasswordCache(String decrypted) {
        Result<SecureItemRqDto.LocalPasswordDto> deserialize =
                Json.deserialize(decrypted, SecureItemRqDto.LocalPasswordDto.class);

        if (deserialize.isFail()) {
            return Result.error(new Exception("Failed to parse password"));
        }

        SecureItemRqDto.LocalPasswordDto localPasswordDto = deserialize.get();
        SecureDetails secureDetails = new SecureDetails(
                localPasswordDto.id(),
                new Category(localPasswordDto.category(), localPasswordDto.category(), "", null, null, null),
                null,
                localPasswordDto.domainDetails() == null ? null : localPasswordDto.domainDetails().domain(),
                "No title",
                localPasswordDto.description(),
                localPasswordDto.isFavourite(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                localPasswordDto.requiresMasterPassword(),
                false

        );

        Password password = new Password(
                localPasswordDto.id(),
                localPasswordDto.username(),
                localPasswordDto.password(),
                secureDetails,
                Action.NEW
        );

        importCache.add(password);

        return Result.Success;
    }

    public Result<ResultRsDto> post(String body) {

        Result<SecureItemRqDto> result = Json.deserialize(body, SecureItemRqDto.class);
        if (result.isFail()) {
            return Result.error(new Exception("Failed to parse SecureItem"));
        }

        SecureItemRqDto secureItemRqDto = result.get();

        Result<String> stringResult = decryptPayload(secureItemRqDto.encryptedPayload());
        if (stringResult.isFail()) {
            return Result.error(new Exception("Failed to decrypt Payload", stringResult.getError()));
        }

        String decrypted = stringResult.get();

        if ("PASSWORD".equals(secureItemRqDto.secureItemTypeId())) {

            Result<Boolean> booleanResult = addToPasswordCache(decrypted);

            if (booleanResult.isFail()) {
                return Result.error(booleanResult.getError());
            }

        }

        return Result.ok(new ResultRsDto("Success", "Uploaded successfully"));
    }


}
