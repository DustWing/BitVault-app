package com.bitvault.services.interfaces;

import com.bitvault.model.Password;
import com.bitvault.util.Result;

import java.util.List;

public interface IPasswordService {
    Result<List<Password>> getPasswords();

    Result<Password> create(Password password);

    Result<Boolean> update(Password password);

    Result<Boolean> delete(Password password);
}
