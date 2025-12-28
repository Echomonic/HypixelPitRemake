package me.themoonis.hypixel.ui.api;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.UUID;
import java.util.function.Consumer;

public interface IUserInterface extends InventoryHolder {

    void open(Player player);

    void close(Player player);

    void loadItems();

    void click(Consumer<InventoryClickEvent> eventConsumer);

    void addPlayer(UUID uuid);
    void removePlayer(UUID uuid);

    void run(InventoryClickEvent event);

    void unload();

    IUserInterfacePhysical getUserInterfaceActions();
}
