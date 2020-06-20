package io.github.kezzyhko.gameoflife;

import java.awt.*;
import java.util.Collection;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class Essentials {
    private Essentials() {}

    /*
    public static <T> T coalesce(Callable<T>... lambdas)
    {
        for (Callable<T> lambda : lambdas) {
            T expr;
            try {
                expr = lambda.call();
            } catch (Exception e) {
                return null;
            }
            if (expr != null) {
                return expr;
            }
        }
        return null;
    }
    */
    public static MatchResult regexpMatches(String regex, String string) {
        return Pattern.compile(regex).matcher(string).toMatchResult();
    }
    public static int intFromInteger(Integer i) {
        return i == null ? 0 : i;
    }

    public static final Color TRANSPARENT_COLOR = new Color(0, true);

    public static String repeat(String str, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

}
