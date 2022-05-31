package io.teach.infrastructure.util;

import java.util.Random;
import java.util.UUID;

public class RandomUtil {

    private static Random random = new Random();

    private static final int NUMERAL_ZERO = 48;
    private static final int NUMERAL_NINE = 57;
    private static final int LETTER_LOWER_A = 97;
    private static final int LETTER_LOWER_Z = 122;
    private static final int LETTER_UPPER_A = 65;
    private static final int LETTER_UPPER_Z = 90;


    public static final String randomAlphabetic(int length) {

        return random.ints(LETTER_LOWER_A, LETTER_LOWER_Z + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static final String randomAlphanumeric(int length) {

        return random.ints(NUMERAL_ZERO, LETTER_LOWER_Z + 1)
                .filter(num -> (num <= NUMERAL_NINE || num >= LETTER_UPPER_A) && (num <= LETTER_UPPER_Z || num >= LETTER_LOWER_A))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static final String randomNumeric(int length) {
        return random.ints(NUMERAL_ZERO, NUMERAL_NINE + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static final String randomUpperAlphabetic(int length) {
        return random.ints(LETTER_UPPER_A, LETTER_UPPER_Z + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    public static final String randomString() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static final String randomToken() {
        return UUID.randomUUID().toString();
    }

}
