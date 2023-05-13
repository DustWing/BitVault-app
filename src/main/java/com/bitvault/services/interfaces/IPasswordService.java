package com.bitvault.services.interfaces;

import com.bitvault.ui.model.Password;
import com.bitvault.util.Result;

import java.util.List;

public interface IPasswordService {
    Result<List<Password>> getPasswords();

    default Result<Password> create(Password password) {
        return Result.ok(password);
    }

    default Result<Password> update(Password password) {
        return Result.ok(password);
    }

    default Result<Boolean> delete(Password password) {
        return Result.Success;
    }
}
