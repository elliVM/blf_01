package com.teragrep.blf_01.tokenizer;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public final class OverlapTokenFinder {
    private static final String regex =
            "(\\t|\\n|\\r| |\\!|\\\"|%0A|%20|%21|%2520|%2526|%26|%28|%29|%2B|%2C|%3A|%3B|%3D|%5B|%5D|%7C|\\'|\\(|\\)|\\*|\\+|,|--|;|<|>|:|\\?|\\[|\\]|\\{|\\||\\}|#|\\$|%|-|\\.|@|\\\\|_|&|/|=)";
    private static final Pattern compiledRegex = Pattern.compile(regex);

    public static String[] find(String input) {

        final Set<String> tokens = new HashSet<>();
        final StringBuilder sb = new StringBuilder();

        for(int i = 0; i < input.length(); i++) {

            if (regexMatch(String.valueOf(input.charAt(i)))) {
                sb.append(input.charAt(i));
                tokens.add(sb.toString());

                for (int j = i+1; j < input.length(); j++) {

                    // add pattern match
                    if (regexMatch(sb.toString()) && (j > i+1)) {
                        tokens.add(sb.toString());
                        tokens.add(sb.deleteCharAt(0).toString());
                        sb.setLength(0);
                        break;
                    }

                    // break if splitter
                    if (regexMatch(String.valueOf(input.charAt(j)))) {
                        sb.setLength(0);
                        break;
                    }

                    sb.append(input.charAt(j));
                }
            }
        }

        String[] rv = new String[tokens.size()];
        return tokens.toArray(rv);

    }
    private static boolean regexMatch(String input) {
        return input.matches(compiledRegex.pattern());
    }
}
