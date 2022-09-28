package com.bitvault.algos;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class ArgonEncoder {


    private final Argon2 argon2;

    public ArgonEncoder() {
        argon2 = Argon2Factory.create(
                Argon2Factory.Argon2Types.ARGON2d,
                32,
                64);
    }

    public String hash(char[] value) {

        final String hash = argon2.hash(
                22,
                65536,
                4,
                value
        );

        argon2.wipeArray(value);

        return hash;
    }

    public boolean verify(final String hash, final char[] value) {

        boolean verify = argon2.verify(hash, value);

        argon2.wipeArray(value);

        return verify;
    }


}
