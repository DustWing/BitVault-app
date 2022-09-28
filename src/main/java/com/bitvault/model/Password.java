package com.bitvault.model;

import com.bitvault.enums.Action;

public record Password(
        String id,
        String username,
        String password,
        SecureDetails secureDetails,
        Action action

) implements Identifiable {

    public Password copyOf() {
        return new Password(
                this.id,
                this.username,
                this.password,
                this.secureDetails,
                this.action
        );
    }

    @Override
    public String getId() {
        return this.id;
    }
}
