package com.bitvault.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
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

    public static String generatePassString(int length) {
        char[] password = new DefaultProvider().getPassword(new char[length]);
        return new String(password);
    }

    /**
     * Complexity value between 0 and 1. 0 means worse.
     */
    public static double passwordComplexity(char[] value) {


        return 0;
    }

    private double charContainsScore(char[] value) {
        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSymbol = false;
        for (char c : value) {
            if (!hasUpperCase && Character.isUpperCase(c)) {
                hasUpperCase = true;
                continue;
            }
            if (!hasLowerCase && Character.isLowerCase(c)) {
                hasLowerCase = true;
                continue;
            }
            if (!hasDigit && Character.isDigit(c)) {
                hasDigit = true;
                continue;
            }

            if (!hasSymbol && isSymbol(c)) {
                hasSymbol = true;
            }
        }

        return 0.0;
    }

    private static boolean isSymbol(char c) {
        for (char symbol : SYMBOLS) {
            if (symbol == c) {
                return true;
            }
        }
        return false;
    }

    private double lengthScore(char[] value) {
        if (value.length <= 4) {
            return 0.0;
        } else if (value.length <= 8) {
            return 0.4;
        } else if (value.length <= 12) {
            return 0.6;
        } else if (value.length < 16) {
            return 0.8;
        }
        return 1.0;

    }

    public static double shannonEntropyCalculator(char[] value) {
        if (value.length == 0) {
            return 0.0;
        }

        Map<Character, Integer> frequency = new HashMap<>();
        for (char c : value) {
            frequency.compute(c, (character, times) -> times == null ? 1 : times + 1);
        }

        double valueLength = ALL_CHARS.length;
        double entropy = 0.0;

        for (Integer times : frequency.values()) {
            double term = (double) times / valueLength;
            entropy -= term * log2(term);
        }
        return entropy * value.length;
    }

    /**
     * Function to calculate the log base 2 of an integer
     */
    private static double log2(double num) {
        return Math.log(num) / Math.log(2);
    }

}
