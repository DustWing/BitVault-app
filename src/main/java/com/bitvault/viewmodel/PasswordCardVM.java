package com.bitvault.viewmodel;

import com.bitvault.model.Password;

public class PasswordCardVM {

    private final Password password;

    public PasswordCardVM(final Password password) {
        this.password = password;

    }

    public Password getPassword() {
        return password;
    }


}
