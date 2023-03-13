package com.bitvault.ui.viewmodel;

import com.bitvault.ui.model.Password;

@Deprecated
public class PasswordCardVM {

    private final Password password;

    public PasswordCardVM(final Password password) {
        this.password = password;

    }

    public Password getPassword() {
        return password;
    }


}
