package io.teach.infrastructure.util;

public class Util {

    private Util(){}

    public static boolean isNull(Object o) {
        return (o == null);
    }

    public static boolean isNotNull(Object o) {
        return (o != null);
    }

    public static boolean isEmpty(String s) {
        if(isNull(s))
            return true;

        return "".equals(s);
    }
}
