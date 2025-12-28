package me.themoonis.hypixel.ui;





import me.themoonis.hypixel.ui.api.UserInterfaceData;

import java.util.concurrent.ConcurrentHashMap;

public class UserInterfaceDataImpl<T> implements UserInterfaceData<T> {

    private final ConcurrentHashMap<String,T> data = new ConcurrentHashMap<>();

    @Override
    public T getContextObject(String key) {
        return data.get(key);
    }

    @Override
    public <C> C getContextObject(String key, Class<C> type) {
        return type.cast(getContextObject(key));
    }

    @Override
    public UserInterfaceData<T> addContextObject(String key, T value) {
        data.put(key,value);
        return this;
    }
    @Override
    public void removeContextObject(String key) {
        if(!isContextObjectValid(key)) return;
        data.remove(key);
    }
    @Override
    public void clearContextObjects() {
        data.clear();
    }
}
