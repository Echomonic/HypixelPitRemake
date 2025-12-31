package me.themoonis.hypixel.player.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SidebarHandler {

    private final HashMap<UUID,PlayerSidebar> sidebarHandler = new HashMap<>();


    public void add(UUID uuid, PlayerSidebar sidebar){
        sidebarHandler.put(uuid, sidebar);
    }
    public void remove(UUID uuid){
        sidebarHandler.remove(uuid);
    }
    public PlayerSidebar get(UUID uuid){
        return sidebarHandler.get(uuid);
    }

    public void clear(){
        sidebarHandler.clear();
    }

    public void start(Plugin plugin){
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<UUID, PlayerSidebar> sidebarEntry : sidebarHandler.entrySet()) {
                sidebarEntry.getValue().tick();
            }
        },0,20);
    }
}
