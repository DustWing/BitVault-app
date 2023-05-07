package com.bitvault.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class ArgonAuthenticator implements Authenticator {

    private final Argon2 argon2;

    public ArgonAuthenticator() {
        argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2d, 32, 64);
    }

    @Override
    public String hash(final char[] value) {
        final String hash = argon2.hash(22, 65536, 4, value);
        argon2.wipeArray(value);
        return hash;
    }

    @Override
    public boolean verify(final String hash, final char[] array) {
        boolean verify = argon2.verify(hash, array);
        argon2.wipeArray(array);
        return verify;
    }

}
