package me.themoonis.hypixel.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PitMapLoader {

    private final Path root;

    @Getter
    private TemporaryGameMap chosenGameMap;


    public void choose(){
        File[] children = root.toFile().listFiles();

        if(children == null) return;

        if(children.length == 0)
            throw new RuntimeException("There are no maps to choose from.");

        int index = ThreadLocalRandom.current().nextInt(0,children.length);

        File chosen = children[index];

        String mapName = chosen.getName().replace(" ","_").toLowerCase();

        Bukkit.getConsoleSender().sendMessage(Text.pit("PIT MAP LOADER","Loaded &b%s&7!", ChatColor.YELLOW,mapName));

        chosenGameMap = new TemporaryGameMap(root.toFile(),mapName,false);
    }
    public boolean loadPitMap(String id, Consumer<Player> action){
        Optional<File> pitMapSearch = getMapDirectoryFromName(id);

        if(!pitMapSearch.isPresent()) return false;

        File pitMapFound = pitMapSearch.get();

        if(chosenGameMap != null && chosenGameMap.isLoaded()) {
            Bukkit.getOnlinePlayers().forEach(action);
            chosenGameMap.unload();
        }
        String tempFolderId = pitMapFound.getName().replace(" ","_").toLowerCase();

        chosenGameMap = new TemporaryGameMap(root.toFile(),tempFolderId,false);
        chosenGameMap.load();
        return true;
    }


    public String[] getMapDirectoryNames(){
        return root.toFile().list();
    }
    public Optional<File> getMapDirectoryFromName(String directoryName){
        return Arrays.stream(root.toFile().listFiles()).filter(file -> file.getName().equalsIgnoreCase(directoryName)).findFirst();
    }
}
