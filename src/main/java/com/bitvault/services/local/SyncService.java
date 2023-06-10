package com.bitvault.services.local;

import com.bitvault.services.interfaces.IPasswordService;
import com.bitvault.services.interfaces.ISyncService;
import com.bitvault.ui.model.Password;
import com.bitvault.util.Result;

import java.util.ArrayList;
import java.util.List;

public class SyncService implements ISyncService {

    private final IPasswordService passwordService;

    public SyncService(IPasswordService passwordService) {
        this.passwordService = passwordService;
    }


    @Override
    public List<Result<Password>> savePasswords(List<Password> passwords) {

        final List<Result<Password>> results = new ArrayList<>(passwords.size());

        for (Password password : passwords) {
            Result<Password> passwordResult = passwordService.create(password);
            results.add(passwordResult);
        }
        return results;
    }
}
