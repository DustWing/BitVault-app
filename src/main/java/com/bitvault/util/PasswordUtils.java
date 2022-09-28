package com.bitvault.util;

import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtils {

    private final static char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    private final static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private final static char[] NUMBERS = "0123456789".toCharArray();
    private final static char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    private final static Random rand = new SecureRandom();

    public interface PasswordProvider {
        char[] getPassword(char[] chars);

    }

    public static class DefaultProvider implements PasswordProvider {
        public char[] getPassword(char[] chars) {

            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == 0) {
                    chars[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
                }
            }

            for (int i = 0; i < chars.length; i++) {
                int randomPosition = rand.nextInt(chars.length);
                char temp = chars[i];
                chars[i] = chars[randomPosition];
                chars[randomPosition] = temp;
            }
            return chars;
        }
    }

    public static class AtLeastOneNumber implements PasswordProvider {
        private final PasswordProvider passwordProvider;

        public AtLeastOneNumber(PasswordProvider passwordProvider) {
            this.passwordProvider = passwordProvider;
        }

        @Override
        public char[] getPassword(char[] chars) {

            addChar(chars, NUMBERS);

            chars = passwordProvider.getPassword(chars);

            return chars;
        }
    }

    public static class AtLeastOneSymbol implements PasswordProvider {

        private final PasswordProvider passwordProvider;

        public AtLeastOneSymbol(PasswordProvider passwordProvider) {
            this.passwordProvider = passwordProvider;
        }

        @Override
        public char[] getPassword(char[] chars) {

            addChar(chars, SYMBOLS);

            chars = passwordProvider.getPassword(chars);

            return chars;
        }
    }

    public static class AtLeastOneUpperCase implements PasswordProvider {

        private final PasswordProvider passwordProvider;

        public AtLeastOneUpperCase(PasswordProvider passwordProvider) {
            this.passwordProvider = passwordProvider;
        }

        @Override
        public char[] getPassword(char[] chars) {

            addChar(chars, UPPERCASE);

            chars = passwordProvider.getPassword(chars);

            return chars;
        }
    }

    public static class AtLeastOneLowerCase implements PasswordProvider {

        private final PasswordProvider passwordProvider;

        public AtLeastOneLowerCase(PasswordProvider passwordProvider) {
            this.passwordProvider = passwordProvider;
        }

        @Override
        public char[] getPassword(char[] chars) {

            addChar(chars, LOWERCASE);

            chars = passwordProvider.getPassword(chars);

            return chars;
        }
    }

    private static void addChar(char[] chars, char[] pattern) {
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 0) {
                chars[i] = pattern[rand.nextInt(pattern.length)];
                break;
            }
        }
    }

    public static char[] generatePass(int length) {
        final PasswordProvider provider = new AtLeastOneNumber(
                new AtLeastOneSymbol(
                        new AtLeastOneUpperCase(
                                new AtLeastOneLowerCase(
                                        new DefaultProvider()
                                )
                        )
                )
        );

        return provider.getPassword(new char[length]);
    }


}
