package me.themoonis.hypixel.ui.button;

import org.apache.logging.log4j.util.TriConsumer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class UserInterfaceButton implements IUserInterfaceButton {

    private final int slot;

    public UserInterfaceButton(int slot) {
        this.slot = slot;
    }
    private ItemStack icon;
    private TriConsumer<Player,ItemStack,Integer> action;

    @Override
    public ItemStack icon() {
        return icon;
    }

    @Override
    public IUserInterfaceButton icon(ItemStack stack) {
        this.icon = stack;
        return this;
    }

    @Override
    public IUserInterfaceButton action(TriConsumer<Player, ItemStack, Integer> action) {
        this.action = action;
        return this;
    }

    @Override
    public void run(InventoryClickEvent event) {
        if(action == null) return;
        if(event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack stack = event.getCurrentItem();
        int slot = event.getSlot();


        action.accept(player,stack,slot);
    }

    @Override
    public int slot() {
        return slot;
    }


    public static UserInterfaceButtonBuilder builder(){
        return new UserInterfaceButtonBuilder();
    }
    public static final class UserInterfaceButtonBuilder{
        private int slot = 0;
        private ItemStack icon = new ItemStack(Material.AIR);
        private TriConsumer<Player,ItemStack,Integer> action = null;

        public UserInterfaceButtonBuilder slot(int slot){
            this.slot = slot;
            return this;
        }
        public UserInterfaceButtonBuilder icon(ItemStack icon){
            this.icon = icon;
            return this;
        }
        public UserInterfaceButtonBuilder action(TriConsumer<Player,ItemStack,Integer> action){
            this.action = action;
            return this;
        }

        public IUserInterfaceButton build(){
            return new UserInterfaceButton(slot).icon(icon).action(action);
        }
    }
}