package com.cliniccare.util;

/** Escapes text before printing it in a JSP, so user input cannot inject HTML/JS. */
public final class HtmlUtil {

    private HtmlUtil() {
    }

    public static String escape(Object value) {

        if (value == null) {
            return "";
        }

        return value.toString()
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
