package me.themoonis.hypixel.commands.parsers;

import dev.echo.concept.api.ArgContext;
import dev.echo.concept.api.ArgumentParser;
import dev.echo.concept.impl.parsers.BooleanParser;
import dev.echo.concept.impl.parsers.NumberParser;
import dev.echo.concept.impl.parsers.StringParser;

import java.util.function.Supplier;

public class ObjectParser implements ArgumentParser<Object> {

    private static final NumberParser<?>[] NUMBER_PARSERS = new NumberParser[]
            {
                    new NumberParser<>(Integer::parseInt, Integer.MIN_VALUE, Integer.MAX_VALUE),
                    new NumberParser<>(Double::parseDouble, Double.MIN_VALUE, Double.MAX_VALUE),
                    new NumberParser<>(Float::parseFloat, Float.MIN_VALUE, Float.MAX_VALUE),
                    new NumberParser<>(Long::parseLong, Long.MIN_VALUE, Long.MAX_VALUE)
            };

    @Override
    public Object output(String s, ArgContext argContext) {
        String trimmed = s.trim();

        if (trimmed.equalsIgnoreCase("true") || trimmed.equalsIgnoreCase("false"))
            return new BooleanParser().output(trimmed, argContext);

        return tryParse(() -> NUMBER_PARSERS[0].output(s, argContext),
                () -> NUMBER_PARSERS[1].output(s, argContext),
                () -> NUMBER_PARSERS[2].output(s, argContext),
                () -> NUMBER_PARSERS[3].output(s, argContext),
                () -> new StringParser().output(s, argContext)
        );
    }

    @SafeVarargs
    private final Object tryParse(Supplier<Object>... parsers) {
        for (Supplier<Object> parser : parsers) {
            try {
                return parser.get();
            } catch (Exception ignored) {
            }
        }
        return null; // Should never reach here if StringParser is last
    }
}
