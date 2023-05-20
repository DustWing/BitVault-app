package com.bitvault.server.endpoints;

import com.bitvault.algos.AES;
import com.bitvault.server.cache.ImportCache;
import com.bitvault.server.dto.KeyDto;
import com.bitvault.server.dto.ResultRsDto;
import com.bitvault.server.dto.SecureItemRqDto;
import com.bitvault.util.Json;
import com.bitvault.util.Result;

import java.util.Base64;

public class SecureItemController implements IGetEndpoint<KeyDto>, IPostEndpoint<ResultRsDto> {

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

        if (deserialize.hasError()) {
            return Result.error(new Exception("Failed to parse password"));
        }

        SecureItemRqDto.LocalPasswordDto localPasswordDto = deserialize.get();


        Result<Boolean> addResult = importCache.add(localPasswordDto);

        if (addResult.hasError()) {
            return Result.error(addResult.getError());
        }
        return Result.Success;
    }

    public Result<ResultRsDto> post(String body) {

        Result<SecureItemRqDto> result = Json.deserialize(body, SecureItemRqDto.class);
        if (result.hasError()) {
            return Result.error(new Exception("Failed to parse SecureItem"));
        }

        SecureItemRqDto secureItemRqDto = result.get();

        Result<String> stringResult = decryptPayload(secureItemRqDto.encryptedPayload());
        if (stringResult.hasError()) {
            return Result.error(new Exception("Failed to decrypt Payload", stringResult.getError()));
        }

        String decrypted = stringResult.get();

        if ("PASSWORD".equals(secureItemRqDto.secureItemTypeId())) {

            Result<Boolean> booleanResult = addToPasswordCache(decrypted);

            if (booleanResult.hasError()) {
                return Result.error(booleanResult.getError());
            }

        }

        return Result.ok(new ResultRsDto("Success", "Uploaded successfully"));
    }


}
