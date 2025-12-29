package me.themoonis.hypixel.player.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;



@Getter
public class PlayerGainXpEvent extends PlayerEvent {

    @Getter
    private static final HandlerList handlerList = new HandlerList();
    private final int gainedXp;

    public PlayerGainXpEvent(Player who,int gainedXp) {
        super(who);
        this.gainedXp = gainedXp;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
