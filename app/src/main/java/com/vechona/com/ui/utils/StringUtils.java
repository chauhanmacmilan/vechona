package com.vechona.com.ui.utils;

public class StringUtils {

    public static String camelCase(String text) {
        if (text == null)
            return null;

        final StringBuilder ret = new StringBuilder(text.length());

        for (final String word : text.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == text.length()))
                ret.append(" ");
        }

        return ret.toString();
    }
}
