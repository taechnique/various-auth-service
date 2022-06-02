package io.teach.infrastructure.controller;

import org.springframework.restdocs.snippet.Attributes;

import static org.springframework.restdocs.snippet.Attributes.key;

public class RestDocsAttributes {

    private RestDocsAttributes() {
        throw new IllegalStateException("Utility class");
    }

    public static Attributes.Attribute length(String length) {
        return key("length").value(length);
    }
    public static Attributes.Attribute length(int length) {
        return key("length").value(String.valueOf(length));
    }

    public static Attributes.Attribute length(int min, int max) {

        StringBuilder sb = new StringBuilder();
        sb.append(min).append("~").append(max);

        return key("length").value(sb.toString());
    }

    public static Attributes.Attribute format(String format) {
        return key("format").value(format);
    }

    public static Attributes.Attribute etc(String etc) {

        if(null == etc || "".equals(etc.trim())) {
            return key("etc").value(etc);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("(").append(" ").append(etc).append(" ").append(")");
            return key("etc").value(sb.toString());

        }
    }
}
