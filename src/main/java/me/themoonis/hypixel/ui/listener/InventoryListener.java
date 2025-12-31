package me.themoonis.hypixel.ui.listener;

import me.themoonis.hypixel.ui.api.IUserInterface;
import me.themoonis.hypixel.ui.api.IUserInterfaceCloseAction;
import me.themoonis.hypixel.ui.api.IUserInterfaceDragAction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;

        InventoryHolder holder = event.getClickedInventory().getHolder();

        if (holder instanceof IUserInterface) {
            IUserInterface userInterface = (IUserInterface) holder;
            userInterface.run(event);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof IUserInterface) {
            IUserInterface userInterface = ((IUserInterface) holder);
            if (userInterface instanceof IUserInterfaceCloseAction) {
                IUserInterfaceCloseAction closeAction = (IUserInterfaceCloseAction) userInterface;
                closeAction.run(event);
                return;
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        InventoryHolder holder = inventory.getHolder();

        if (holder instanceof IUserInterface) {
            IUserInterface userInterface = ((IUserInterface) holder);
            if (userInterface instanceof IUserInterfaceDragAction) {
                IUserInterfaceDragAction closeAction = (IUserInterfaceDragAction) userInterface;
                closeAction.run(event);
                return;
            }
        }
    }
}

