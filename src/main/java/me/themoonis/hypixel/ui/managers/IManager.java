package me.themoonis.hypixel.ui.managers;

public interface IManager<K,V> {

    void add(K key, V value);

    V get(K key);


    default boolean exists(K key){
        return get(key) != null;
    }

    void remove(K key);
    void clear();

}
