package me.themoonis.hypixel.json.trackers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import me.themoonis.hypixel.json.Jsons;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.PlayerJsonDTO;
import me.themoonis.hypixel.player.enums.PlayerRank;
import org.apache.commons.io.FileUtils;
import org.bukkit.OfflinePlayer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PlayerDataTracker {

    private final ObjectNode root;
    private final Path rootPath;
    private final Executor io = Executors.newSingleThreadExecutor();
    private final Map<UUID, PlayerJson> trackerMap = new ConcurrentHashMap<>();

    @SneakyThrows
    public PlayerDataTracker(Path root) {
        if (!Files.exists(root)) Files.createDirectories(root);
        Path playerJson = root.resolve("players.json");

        if (!Files.exists(playerJson)) {
            Files.createFile(playerJson);
            Files.write(playerJson,"{}".getBytes(StandardCharsets.UTF_8));
        }

        this.rootPath = playerJson;
        this.root = (ObjectNode) Jsons.MAPPER.readTree(FileUtils.readFileToString(playerJson.toFile(), StandardCharsets.UTF_8));
    }

    public PlayerJson getOrCreate(OfflinePlayer player) {
        return trackerMap.computeIfAbsent(player.getUniqueId(), uuid -> PlayerJson.fromDomain(
                new PlayerJsonDTO(
                        uuid,
                        player.getName(),
                        PlayerRank.DEFAULT,
                        1,
                        0,
                        0,
                        0,
                        0,
                        0,
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new HashMap<>())
        ));
    }

    public PlayerJson get(UUID player) {
        return trackerMap.computeIfAbsent(player, this::loadPlayerSync);
    }

    public void put(UUID player, PlayerJson object) {
        trackerMap.put(player, object);
        saveAsync(player);
    }

    private void saveAsync(UUID player) {
        PlayerJson snapshot = trackerMap.computeIfAbsent(player, this::loadPlayerSync);
        io.execute(() -> {
            root.set(player.toString(), Jsons.MAPPER.valueToTree(snapshot.toDto()));
            try {
                Jsons.MAPPER.writerWithDefaultPrettyPrinter().writeValue(rootPath.toFile(), root);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private PlayerJson loadPlayerSync(UUID p) {
        JsonNode playerNode = root.get(p.toString());
        PlayerJsonDTO playerJson = Jsons.MAPPER.convertValue(playerNode, PlayerJsonDTO.class);
        return PlayerJson.fromDomain(playerJson);
    }

}
