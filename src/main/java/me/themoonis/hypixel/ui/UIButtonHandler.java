package me.themoonis.hypixel.ui;

import me.themoonis.hypixel.ui.api.IUserInterface;
import me.themoonis.hypixel.ui.api.IUserInterfacePhysical;
import me.themoonis.hypixel.ui.button.IUserInterfaceButton;
import me.themoonis.hypixel.ui.managers.IManager;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ConcurrentHashMap;

public class UIButtonHandler implements IManager<Integer, IUserInterfaceButton> {

    private final ConcurrentHashMap<Integer, IUserInterfaceButton> buttons = new ConcurrentHashMap<>();

    @Override
    public void add(Integer key, IUserInterfaceButton value) {
        buttons.put(key, value);
    }

    @Override
    public IUserInterfaceButton get(Integer key) {
        return buttons.get(key);
    }

    @Override
    public void remove(Integer key) {
        if (!exists(key)) return;

        buttons.remove(key);
    }

    @Override
    public void clear() {
        buttons.clear();
    }

    public void add(IUserInterfaceButton button) {
        add(button.slot(), button);
    }


    public boolean updateButton(int slot, IUserInterface userInterface) {
        IUserInterfaceButton button = get(slot);
        if (button == null)
            return false;

        ItemStack icon = button.icon();

        if (icon == null) return false;
        IUserInterfacePhysical actions = userInterface.getUserInterfaceActions();

        if (icon.getType() != Material.AIR)
            actions.setItem(icon, button.slot());

        return true;
    }

    private void updateButton(IUserInterfaceButton button, IUserInterfacePhysical iUserInterfacePhysical) {
        if (button == null) return;

        ItemStack icon = button.icon();

        if (icon == null) return;

        if (icon.getType() != Material.AIR)
            iUserInterfacePhysical.setItem(icon, button.slot());
    }

    public void runButton(int slot, InventoryClickEvent event) {
        runButton(get(slot), event);
    }
    public void runButton(IUserInterfaceButton button, InventoryClickEvent event) {
        if (button == null) return;

        button.run(event);
    }
    public void updateButtons(IUserInterface userInterface) {
        IUserInterfacePhysical actions = userInterface.getUserInterfaceActions();
        buttons.values().forEach(button -> updateButton(button, actions));
    }
}
