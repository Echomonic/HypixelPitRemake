package me.themoonis.hypixel.ui.api;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface IUserInterfacePhysical {


    void clear();

    int size();

    void setItem(ItemStack stack, int slot);
    void addItem(ItemStack... stacks);
    Optional<ItemStack> getItem(int slot);
    void clearItem(int slot);

}
