package com.bitvault.ui.model;

public record User(
        String id,
        String name,
        String credentials
) {

    public User forLogin(String name, String credentials){
        return new User(
                null,
                this.name,
                credentials
        );
    }
    public User withNewCredentials(final String credentials) {
        return new User(
                this.id,
                this.name,
                credentials
        );
    }
}
