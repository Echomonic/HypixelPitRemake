package me.themoonis.hypixel.commands.parsers;

import dev.echo.concept.api.ArgContext;
import dev.echo.concept.api.ArgumentParser;
import org.bukkit.Location;

public class LocationParser implements ArgumentParser<Location> {
    @Override
    public Location output(String s, ArgContext argContext) {
        String searchType = argContext.getSettingOr("type", "close", String.class);
        boolean isLocal = argContext.getSettingOr("local", true, Boolean.class);

        Location location = null;


        if (searchType.equalsIgnoreCase("close")){
            String[] params = s.split(",");

            double x = Double.parseDouble(params[0]);
            double y = Double.parseDouble(params[1]);
            double z = Double.parseDouble(params[2]);
            if(isLocal && params.length == 3) {

            }else if(!isLocal && params.length == 4){

            }
        }
            return location;
    }
}
