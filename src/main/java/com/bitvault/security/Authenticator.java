package com.bitvault.security;

public interface Authenticator {

    String hash(char[] value);

    boolean verify(final String hash,  final char[] array);
}
