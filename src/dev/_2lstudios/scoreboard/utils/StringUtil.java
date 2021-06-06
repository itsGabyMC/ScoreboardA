package dev._2lstudios.scoreboard.utils;

public class StringUtil {
    public static String trimToLength(final String string, final int length) {
        return string.substring(0, Math.min(length, string.length()));
    }
}
