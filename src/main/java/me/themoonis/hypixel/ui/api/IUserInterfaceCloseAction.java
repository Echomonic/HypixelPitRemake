package me.themoonis.hypixel.ui.api;

import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.function.Consumer;

public interface IUserInterfaceCloseAction {

    void close(Consumer<InventoryCloseEvent> eventConsumer);

    void run(InventoryCloseEvent event);
}
