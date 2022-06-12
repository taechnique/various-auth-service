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

    private final static String emailRegex = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
    private final static String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,20}";
    private final static String uuidRegex = "^([a-fA-F0-9]{8})([-]?)([a-fA-F0-9]{4})([-]?)(4[a-fA-F0-9]{3})([-]?)([a-fA-F0-9]{4})([-]?)([a-fA-F0-9]{12})$";
    private final static String phoneRegex = "^(01[0|1|6|7|8|9])(-)?([0-9]{3,4})(-)?([0-9]{4})$";



    public static boolean email(final String email) {

        final Pattern compiled = Pattern.compile(emailRegex);
        final Matcher matcher = compiled.matcher(email);

        return matcher.matches();
    }

    public static boolean password(final String password) {

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

        final Pattern compiled = Pattern.compile(uuidRegex);
        final Matcher matcher = compiled.matcher(uuid);

        return matcher.matches();
    }


    public static String phone(final String phone, final boolean needHyphen) {
        final Pattern compiled = Pattern.compile(phoneRegex);
        final Matcher matcher = compiled.matcher(phone);
        final String delim = needHyphen ? "-" : "";

        if( ! matcher.matches()) {
            return null;
        }

        return String.join(delim, matcher.group(1), matcher.group(3), matcher.group(5));
    }
}
