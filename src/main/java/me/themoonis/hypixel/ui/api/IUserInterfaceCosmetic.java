package me.themoonis.hypixel.ui.api;

import me.themoonis.hypixel.ui.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public interface IUserInterfaceCosmetic {

    void border(ItemStack stack);

    void fill(ItemStack stack);

    default void border(Material material) {
        ItemStack stack = ItemBuilder.build(material, itemBuilder -> {
            itemBuilder.setDisplayName("");
            itemBuilder.addItemFlags(ItemFlag.values());
        });

        ItemMeta meta = stack.getItemMeta();
        stack.setItemMeta(meta);
        border(stack);
    }
    default void fill(Material material){
        ItemStack stack = ItemBuilder.build(material, itemBuilder -> {
            itemBuilder.setDisplayName("");
            itemBuilder.addItemFlags(ItemFlag.values());
        });

        ItemMeta meta = stack.getItemMeta();
        stack.setItemMeta(meta);
        fill(stack);
    }
}

