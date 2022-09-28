package com.bitvault.model;

public record User(
        String id,
        String name,
        String credentials
) {


    public User copy(final String credentials) {
        return new User(
                this.id,
                this.name,
                credentials
        );
    }
}
