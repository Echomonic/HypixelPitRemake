package me.themoonis.hypixel.ui.managers.impl;

import lombok.Getter;
import me.themoonis.hypixel.ui.api.IUserInterface;
import me.themoonis.hypixel.ui.managers.IManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager implements IManager<UUID, IUserInterface> {

    private final ConcurrentHashMap<UUID, IUserInterface> players = new ConcurrentHashMap<>();


    @Getter
    private final JavaPlugin plugin;

    public PlayerManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @Override
    public void add(UUID key, IUserInterface value) {
        players.put(key, value);
    }

    @Override
    public IUserInterface get(UUID key) {
        return players.get(key);
    }

    @Override
    public void remove(UUID key) {
        players.remove(key);
    }

    public Collection<IUserInterface> values(){
        return players.values();
    }

    public Set<Map.Entry<UUID, IUserInterface>> entries(){
        return players.entrySet();
    }
    public Set<UUID> keys(){
        return players.keySet();
    }

    @Override
    public void clear() {
        players.clear();
    }
}
