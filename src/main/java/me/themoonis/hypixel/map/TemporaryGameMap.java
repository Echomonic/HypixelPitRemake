package me.themoonis.hypixel.map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.SneakyThrows;
import me.themoonis.hypixel.json.Jsons;
import me.themoonis.hypixel.utils.Text;
import me.themoonis.hypixel.utils.WorldPoint;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.HashMap;

public class TemporaryGameMap {

    private final File sourceWorldFolder;
    private File activeWorldFolder;

    private World bukkitWorld;
    private MapConfiguration mapConfiguration;

    public TemporaryGameMap(File worldFolder, String worldName, boolean loadOnInit) {
        this.sourceWorldFolder = new File(worldFolder, worldName);
        if (loadOnInit) load();
    }


    @SneakyThrows
    public boolean load() {
        if (isLoaded()) return true;

        this.activeWorldFolder = new File(Bukkit.getWorldContainer().getParentFile(), sourceWorldFolder.getName() + "_active_" + System.currentTimeMillis());

        FileUtils.copyDirectory(sourceWorldFolder, activeWorldFolder);

        loadConfiguration();

        this.bukkitWorld = Bukkit.createWorld(new WorldCreator(activeWorldFolder.getName()));
        if (bukkitWorld != null)
            this.bukkitWorld.setAutoSave(false);


        return isLoaded();
    }

    @SneakyThrows
    public void unload() {
        if (bukkitWorld != null) Bukkit.unloadWorld(bukkitWorld, false);
        if (activeWorldFolder != null) FileUtils.deleteDirectory(activeWorldFolder);

        saveConfiguration();
        mapConfiguration = null;
        bukkitWorld = null;
        activeWorldFolder = null;
    }
    @SneakyThrows
    public void saveConfiguration(){
        File configurationFile = new File(sourceWorldFolder,"configuration.json");
        //I don't know how it would get here because it makes one on load.
        if(!configurationFile.exists()){
            Bukkit.getConsoleSender().sendMessage(Text.pit("PIT MAP","Somehow the map doesn't have a configuration file.",ChatColor.DARK_AQUA));
            return;
        }
        Jsons.MAPPER.writerWithDefaultPrettyPrinter().writeValue(configurationFile,mapConfiguration);
    }
    public void restoreFromSource() {
        unload();
        load();
    }

    public boolean isLoaded() {
        return getWorld() != null;
    }

    public World getWorld() {
        return bukkitWorld;
    }

    public MapConfiguration getMapConfiguration() {
        if(this.mapConfiguration == null) {
            loadConfiguration();
            return mapConfiguration;
        }

        return mapConfiguration;
    }

    @SneakyThrows
    private void loadConfiguration(){
        File configurationFile = new File(sourceWorldFolder,"configuration.json");

        if(!configurationFile.exists()) {
            Bukkit.getConsoleSender().sendMessage(Text.pit("PIT MAP LOADER",
                    "No configuration was found for: " + sourceWorldFolder.getName(),
                    ChatColor.YELLOW));
            Bukkit.getConsoleSender().sendMessage(Text.pit("PIT MAP LOADER","Creating configuration for map...",ChatColor.YELLOW));
            MapConfiguration defaultConfiguration = new MapConfiguration(
                    new WorldPoint(0,100,0,0,90),
                    new WorldPoint(0,100,0,0,90),
                    new HashMap<>());

            configurationFile.createNewFile();

            Jsons.MAPPER.writerWithDefaultPrettyPrinter().writeValue(configurationFile,defaultConfiguration);

            this.mapConfiguration = defaultConfiguration;
            return;
        }
        this.mapConfiguration = Jsons.MAPPER.readValue(configurationFile, MapConfiguration.class);
        Bukkit.getConsoleSender().sendMessage(Text.pit("PIT MAP LOADER","Loaded configuration for &a%s&7.",ChatColor.YELLOW, sourceWorldFolder.getName()));
    }
}
