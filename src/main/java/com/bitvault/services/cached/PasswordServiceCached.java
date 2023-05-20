package com.bitvault.services.cached;

import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.ui.model.Password;
import com.bitvault.util.Result;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PasswordServiceCached implements IPasswordService {

    private final Map<String, Password> cache = new ConcurrentHashMap<>();
    private final IPasswordService passwordService;

    public PasswordServiceCached(IPasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @Override
    public Result<List<Password>> getPasswords() {

        if (cache.isEmpty()) {
            Result<List<Password>> passwordsResult = passwordService.getPasswords();
            if (passwordsResult.hasError()) {
                return passwordsResult;
            }
            final Map<String, Password> map = passwordsResult.get().stream()
                    .collect(Collectors.toMap(Password::getId, Function.identity()));

            this.cache.putAll(map);

            return passwordsResult;
        }

        return Result.ok(List.copyOf(cache.values()));
    }

    @Override
    public Result<Password> create(Password password) {
        Result<Password> passwordResult = passwordService.create(password);
        if (passwordResult.hasError()) {
            return passwordResult;
        }
        Password newPass = passwordResult.get();
        this.cache.put(newPass.getId(), newPass);
        return passwordResult;
    }

    @Override
    public Result<Password> update(Password password) {
        Result<Password> passwordResult = passwordService.update(password);
        if (passwordResult.hasError()) {
            return passwordResult;
        }
        Password newPass = passwordResult.get();
        this.cache.put(newPass.getId(), newPass);
        return passwordResult;
    }

    @Override
    public Result<Boolean> delete(Password password) {

        Result<Boolean> deleteResult = passwordService.delete(password);
        if (deleteResult.hasError()) {
            return deleteResult;
        }
        this.cache.remove(password.getId());
        return deleteResult;
    }
}
