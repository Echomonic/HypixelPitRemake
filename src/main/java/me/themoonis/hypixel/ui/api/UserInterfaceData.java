package me.themoonis.hypixel.ui.api;





import me.themoonis.hypixel.ui.UserInterfaceDataImpl;

import java.util.function.Consumer;

public interface UserInterfaceData<T> {

    T getContextObject(String key);
    <C> C getContextObject(String key,Class<C> type);

    UserInterfaceData<T> addContextObject(String key, T value);

    default boolean isContextObjectInstance(String key, Class<?> type){
        if(!isContextObjectValid(key)) {
            return false;
        }
        T value = getContextObject(key);
        Class<?> tClass = value.getClass();
        return tClass.isAssignableFrom(type);
    }

    default boolean isContextObjectValid(String key) {
        return getContextObject(key) != null;
    }

    void removeContextObject(String key);

    void clearContextObjects();


    static UserInterfaceData<Object> create() {
        return new UserInterfaceDataImpl<>();
    }
    static UserInterfaceData<Object> create(Consumer<UserInterfaceData<Object>> dataConsumer){
        UserInterfaceData<Object> created = new UserInterfaceDataImpl<>();
        dataConsumer.accept(created);
        return created;
    }
}
