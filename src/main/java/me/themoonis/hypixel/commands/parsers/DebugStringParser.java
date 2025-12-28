package me.themoonis.hypixel.commands.parsers;

import dev.echo.concept.api.ArgContext;
import dev.echo.concept.api.ArgumentParser;

public class DebugStringParser implements ArgumentParser<String> {
    @Override
    public String output(String raw, ArgContext context) {
        String setting = context.getSettingOr("type", "string", String.class);


        String[] args = context.allArgs;
        int start = context.currentIndex;

        if (start >= args.length) return null;

        String first = args[start];

        if (first.isEmpty()) return "";

        if (setting.equalsIgnoreCase("string")) {

            char quoteChar = first.charAt(0);
            boolean isQuoted = quoteChar == '"' || quoteChar == '\'';

            if (!isQuoted) return first;

            if (first.length() > 1 && first.charAt(first.length() - 1) == quoteChar) {
                return first.substring(1, first.length() - 1);
            }

            StringBuilder builder = new StringBuilder(first);
            for (int i = start + 1; i < args.length; i++) {
                builder.append(' ').append(args[i]);
                if (!args[i].isEmpty() && args[i].charAt(args[i].length() - 1) == quoteChar) {
                    String full = builder.toString();

                    return full.substring(1, full.length() - 1);
                }
            }
            return builder.toString();
        } else if (setting.equalsIgnoreCase("greedy")) {
            StringBuilder builder = new StringBuilder();

            for (String s : context.getArgsFromCurrent()) {
                builder.append(s).append(' ');
            }

            return builder.toString().trim();
        } else return first;
    }
}
