package io.teach.infrastructure.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtil {

    private ValidUtil() {}

    public static boolean email(final String email) {

        final String emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
        final Pattern compiled = Pattern.compile(emailRegex);
        final Matcher matcher = compiled.matcher(email);

        return matcher.matches();
    }
}
