package me.themoonis.hypixel.listeners;

import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.map.PitMapLoader;
import me.themoonis.hypixel.map.TemporaryGameMap;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.TagHandler;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.game.PlayerLevel;
import me.themoonis.hypixel.player.game.PlayerPrestige;
import me.themoonis.hypixel.player.util.PlayerPackets;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.NumberFormat;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerListener implements Listener {

    private final PlayerDataTracker tracker;
    private final TemporaryGameMap gameMap;
    private final TagHandler tagHandler;
    private final HypixelPitRemake plugin;


    public PlayerListener(HypixelPitRemake plugin) {
        this.plugin = plugin;
        this.gameMap = plugin.getGameMap();
        this.tracker = plugin.getPlayerDataTracker();
        this.tagHandler = plugin.getTagHandler();
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(gameMap.getMapConfiguration().getSpawnPoint().toBukkitPoint(gameMap.getWorld()));
        tracker.put(player.getUniqueId(), tracker.getOrCreate(player));
        tagHandler.generatePrefix(player);
        tagHandler.displayPlayerTag(player);
        tagHandler.displayOtherTags(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        tagHandler.removePlayerTag(player);
        tracker.put(player.getUniqueId(), tracker.getOrCreate(player));
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PlayerJson playerJson = tracker.get(player.getUniqueId());

        event.setCancelled(true);

        PlayerRank rank = playerJson.getRank();
        PlayerLevel level = playerJson.getLevel();
        PlayerPrestige prestige = playerJson.getPrestige();
        ChatColor plusColor = ChatColor.valueOf(playerJson.getData().computeIfAbsent("plus_color", o -> ChatColor.RED.name()).toString());
        boolean hasPrestige = prestige.getRawNumber() != 0;


        String playerInput = event.getMessage();

        StringBuilder message = new StringBuilder();

        if (rank == PlayerRank.ADMIN)
            playerInput = Text.color(playerInput);

        message.append(hasPrestige ? prestige.getFormattedText(true) : level.getFormattedText(true))
                .append(rank == PlayerRank.DEFAULT ? "" : " " + rank.toString(plusColor))
                .append(" ")
                .append(player.getName())
                .append(rank == PlayerRank.DEFAULT ? ChatColor.GRAY : ChatColor.WHITE)
                .append(": ")
                .append(playerInput);

        event.getRecipients().forEach(online -> online.sendMessage(Text.color(message.toString())));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setDeathMessage(null);
        event.setDroppedExp(0);

        Player player = event.getEntity();
        Player killer = player.getKiller();
        if (killer == null) return;

        PlayerJson killerJson = tracker.get(killer.getUniqueId());
        PlayerJson playerJson = tracker.get(player.getUniqueId());

        PlayerPackets.bar(killer, playerJson.getRank().getColor() + player.getName() + " &a&lKILL!");
        int givenXp = 5 + ThreadLocalRandom.current().nextInt(5, 10);
        double givenGold = Math.round(ThreadLocalRandom.current().nextDouble(5, 25));

        //For updating the scoreboard (when I add one.) we'll just subtract to get the difference.
        if (killerJson.getLevel().getRawNumber() != 120) {
            killerJson.setXp(killerJson.getXp() + givenXp);

            int levelXp = killerJson.isPrestige() ? killerJson.getPrestige().getPrestigeXp() : killerJson.getLevel().getLevelXp();

            int xpNeeded = levelXp - killerJson.getXp();

            if(xpNeeded > 0) return;

            String currentLevel = killerJson.getFormattedLevel(false,true);
            killerJson.setLevel(killerJson.getLevel().getRawNumber() + 1);
            String nextLevel = killerJson.getFormattedLevel(false,true);

            PlayerPackets.title(killer, "&b&lLEVEL UP!",
                    currentLevel + "&f➟" + nextLevel
            );
            killerJson.setXp(0);
            tagHandler.refreshPlayerTag(killer);
        }

        killerJson.setGold(killerJson.getGold() + givenGold);
        killerJson.getData().put("all_gold", (int) killerJson.getData().computeIfAbsent("all_gold", s -> 0) + givenGold);

        String message = String.format("on %s &b+%sXP &6+%sg",
                playerJson.getFormattedLevel(false, true) + " " + playerJson.getRank().getColor() + player.getName(),
                givenXp, NumberFormat.getInstance().format(givenGold));

        killer.sendMessage(Text.pit("KILL", message, ChatColor.GREEN));

        killer.playSound(killer.getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 2);

        player.spigot().respawn();
        player.teleport(gameMap.getMapConfiguration().getSpawnPoint().toBukkitPoint(gameMap.getWorld()));
    }

}
