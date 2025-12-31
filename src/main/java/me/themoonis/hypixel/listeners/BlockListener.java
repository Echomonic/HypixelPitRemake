package me.themoonis.hypixel.listeners;

import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.enums.PlayerSetting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
public class BlockListener implements Listener {

    private final HypixelPitRemake plugin;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        PlayerJson player = plugin.getPlayerDataTracker().get(event.getPlayer().getUniqueId());

        List<PlayerSetting> settings = (List<PlayerSetting>) player.getData()
                .computeIfAbsent("admin_settings", s -> new ArrayList<PlayerSetting>());

        if(!settings.contains(PlayerSetting.BUILD_MODE))
            event.setCancelled(true);
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerJson player = plugin.getPlayerDataTracker().get(event.getPlayer().getUniqueId());
        List<PlayerSetting> settings = (List<PlayerSetting>) player.getData()
                .computeIfAbsent("admin_settings", s -> new ArrayList<PlayerSetting>());

        if(!settings.contains(PlayerSetting.BUILD_MODE))
            event.setCancelled(true);
    }
    @EventHandler
    public void onDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }
}
