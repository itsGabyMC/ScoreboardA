package dev._2lstudios.scoreboard.utils;

import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class SideboardExpressions {
    private final static Pattern equalsIgnoreCase = Pattern.compile("==");
    private final static Pattern equals = Pattern.compile("===");
    private final static Pattern notEqualsIgnoreCase = Pattern.compile("!=");
    private final static Pattern notEquals = Pattern.compile("!==");

    // Expressions
    public static boolean endExpression(String line) {
        final String value = line.split("_")[1];
        return ChatColor.stripColor(line).endsWith(value);
    }

    public static boolean equalsExpression(String line) {
        final String[] split = equalsIgnoreCase.split(line);
        final String first = split[0];
        final String second = split[1];

        return first.equalsIgnoreCase(second);
    }

    public static boolean exactExpression(String line) {
        final String[] split = equals.split(line);
        final String first = split[0];
        final String second = split[1];

        return first.equals(second);
    }

    public static boolean notEqualsExpression(String line) {
        final String[] split = notEqualsIgnoreCase.split(line);
        final String first = split[0];
        final String second = split[1];

        return !first.equalsIgnoreCase(second);
    }

    public static boolean notExactExpression(String line) {
        final String[] split = notEquals.split(line);
        final String first = split[0];
        final String second = split[1];

        return !first.equals(second);
    }

    public static boolean notExpression(final String expression, final String line) {
        final String value = expression.split("_")[1];
        return !ChatColor.stripColor(line).endsWith(value);
    }

    // Utils
    public static String extractExpression(String line) {
        if (line.startsWith("[") && line.contains("]")) {
            return line.split("\\[")[1].split("\\]")[0];
        }
        return null;
    }

    public static boolean evaluateExpression(String line) {
        if (line.contains("[display=false]")) {
            return false;
        }

        final String expression = SideboardExpressions.extractExpression(line);

        if (expression != null) {
            if (expression.startsWith("end_")) {
                return SideboardExpressions.endExpression(line);
            }

            else if (expression.startsWith("not_")) {
                return SideboardExpressions.notExpression(expression, line);
            }

            else if (expression.contains("!==")) {
                return SideboardExpressions.notExactExpression(line);
            }

            else if (expression.contains("!=")) {
                return SideboardExpressions.notEqualsExpression(line);
            }

            else if (expression.contains("===")) {
                return SideboardExpressions.exactExpression(line);
            }

            else if (expression.contains("==")) {
                return SideboardExpressions.equalsExpression(line);
            }
        }

        return true;
    }

    public static String stripExpressions(String line) {
        final String extractedExpression = extractExpression(line);

        if (extractedExpression != null) {
            return line.replace("[" + extractExpression(line) + "]", "");
        } else {
            return line;
        }
    }

}
