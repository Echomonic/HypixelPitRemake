package me.themoonis.hypixel.commands.parsers;

import dev.echo.concept.api.ArgContext;
import dev.echo.concept.api.ArgumentParser;
import me.themoonis.hypixel.player.enums.PlayerRank;
public class PlayerRankParser implements ArgumentParser<PlayerRank> {

    @Override
    public PlayerRank output(String s, ArgContext argContext) {
        boolean forceUppercase = argContext.getSettingOr("force-uppercase", false, Boolean.class);

        if(forceUppercase)
            s = s.toUpperCase();

        try{
            return PlayerRank.valueOf(s);
        } catch (IllegalArgumentException exception){
            return PlayerRank.DEFAULT;
        }
    }
}
