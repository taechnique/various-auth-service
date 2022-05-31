package io.teach.infrastructure.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

    private JSONUtil() {}
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String toJSON(final Object target) {
        if(Util.isNull(target))
            return null;

        return gson.toJson(target);
    }
}
