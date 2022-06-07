package io.teach.infrastructure.util;


import io.teach.infrastructure.excepted.AuthorizingException;
import io.teach.infrastructure.excepted.ServiceStatus;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Optional;
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

    public static boolean password(final String password) {

        final String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}";
        final Pattern compiled = Pattern.compile(passwordRegex);
        final Matcher matcher = compiled.matcher(password);

        return matcher.matches();
    }

    public static boolean password(final String password, final String passwordConfirm) {
        return password(password) && password.equals(passwordConfirm);
    }

    public static Optional<Boolean> checkEssentialEntries(final Boolean... essential) {

        return Arrays.stream(essential)
                .filter((b) -> ( ! b.booleanValue()))
                .findFirst();
    }

    public static boolean uuid(final String uuid) {

        final String uuidRegex = "^([a-fA-F0-9]{8})([-]?)([a-fA-F0-9]{4})([-]?)(4[a-fA-F0-9]{3})([-]?)([a-fA-F0-9]{4})([-]?)([a-fA-F0-9]{12})$";
        final Pattern compiled = Pattern.compile(uuidRegex);
        final Matcher matcher = compiled.matcher(uuid);

        return matcher.matches();
    }
}
