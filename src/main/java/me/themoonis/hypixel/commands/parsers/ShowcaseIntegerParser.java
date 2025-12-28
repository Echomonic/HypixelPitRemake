package me.themoonis.hypixel.commands.parsers;

import dev.echo.concept.api.ArgContext;
import dev.echo.concept.api.ArgumentParser;

public class ShowcaseIntegerParser implements ArgumentParser<Integer> {
    @Override
    public Integer output(String s, ArgContext argContext) {
        int minimum = argContext.getSettingOr("minimum",Integer.MIN_VALUE,Integer.class);
        int maximum = argContext.getSettingOr("maximum",Integer.MAX_VALUE,Integer.class);

        try{
            int outputNumber = Integer.parseInt(s);
            return Math.max(minimum,Math.min(maximum,outputNumber));
        } catch (NumberFormatException exception){
            return -1;
        }
    }
}
