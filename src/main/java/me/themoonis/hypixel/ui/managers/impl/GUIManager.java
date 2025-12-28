package me.themoonis.hypixel.ui.managers.impl;





import me.themoonis.hypixel.ui.api.IUserInterface;
import me.themoonis.hypixel.ui.managers.IManager;

import java.util.HashMap;

public class GUIManager implements IManager<Class<? extends IUserInterface>, IUserInterface> {

    private final HashMap<Class<? extends IUserInterface>, IUserInterface> inventories = new HashMap<>();


    @Override
    public void add(Class<? extends IUserInterface> key, IUserInterface value) {
        inventories.put(key, value);
    }

    @Override
    public IUserInterface get(Class<? extends IUserInterface> key) {
        return inventories.get(key);
    }
    public <I extends IUserInterface> I get(Class<? extends IUserInterface> key, Class<I> type) {
        return type.cast(inventories.get(key));
    }
    @Override
    public void remove(Class<? extends IUserInterface> key) {
        inventories.remove(key);
    }



    @Override
    public void clear() {
        inventories.clear();
    }
}
