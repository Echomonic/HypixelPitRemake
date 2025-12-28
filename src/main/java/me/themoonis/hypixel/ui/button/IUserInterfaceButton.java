package me.themoonis.hypixel.ui.button;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface IUserInterfaceButton {

    ItemStack icon();

    IUserInterfaceButton icon(ItemStack stack);

    default IUserInterfaceButton icon(Material material) {
        return icon(new ItemStack(material));
    }

    IUserInterfaceButton action(TriConsumer<Player, ItemStack, Integer> action);

    void run(InventoryClickEvent event);

    int slot();

}
