package me.themoonis.hypixel.listeners;

import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.map.TemporaryGameMap;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.TagHandler;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.events.PlayerGainXpEvent;
import me.themoonis.hypixel.player.game.PlayerLevel;
import me.themoonis.hypixel.player.game.PlayerPrestige;
import me.themoonis.hypixel.player.sidebar.PlayerSidebar;
import me.themoonis.hypixel.player.util.PlayerPackets;
import me.themoonis.hypixel.utils.Text;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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

        Sidebar sidebar = plugin.getScoreboardLibrary().createSidebar();
        sidebar.addPlayer(player);

        plugin.getSidebarHandler().add(player.getUniqueId(),new PlayerSidebar(tracker.get(player.getUniqueId()),sidebar));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        tagHandler.removePlayerTag(player);

        plugin.getSidebarHandler().get(player.getUniqueId()).close();
        plugin.getSidebarHandler().remove(player.getUniqueId());

        tracker.put(player.getUniqueId(), tracker.getOrCreate(player));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(gameMap.getMapConfiguration().getSpawnPoint().toBukkitPoint(gameMap.getWorld()));
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

        if (killerJson.getLevel().getRawNumber() != 120)
            Bukkit.getPluginManager().callEvent(new PlayerGainXpEvent(killer, givenXp));

        killerJson.setGold(killerJson.getGold() + givenGold);
        killerJson.getData().put("all_gold", (int) killerJson.getData().computeIfAbsent("all_gold", s -> 0) + givenGold);

        String message = String.format("on %s &b+%sXP &6+%sg",
                playerJson.getFormattedLevel(false, true) + " " + playerJson.getRank().getColor() + player.getName(),
                givenXp, NumberFormat.getInstance().format(givenGold));

        killer.sendMessage(Text.pit("KILL", message, ChatColor.GREEN));

        killer.playSound(killer.getLocation(), Sound.SUCCESSFUL_HIT, 0.5f, 2);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.spigot().respawn();
            player.teleport(gameMap.getMapConfiguration().getSpawnPoint().toBukkitPoint(gameMap.getWorld()));
        }, 1);
    }

    @EventHandler
    public void onPlayerXpGain(PlayerGainXpEvent event) {
        Player killer = event.getPlayer();
        int givenXp = event.getGainedXp();
        PlayerJson killerJson = tracker.get(killer.getUniqueId());

        int killerXp = killerJson.getXp();
        killerJson.setXp(killerXp + givenXp);

        String currentLevel = killerJson.getFormattedLevel(false, true);

        handleLevelUp(killer,currentLevel, killerJson);
    }

    private void handleLevelUp(Player killer,String currentLevel, PlayerJson killerJson) {
        if (killerJson.getLevel().getRawNumber() == 120){
            killerJson.setXp(0);
            return;
        }

        int levelXp = killerJson.isPrestige() ? killerJson.getPrestige().getPrestigeXp() : killerJson.getLevel().getLevelXp();
        int killerXp = killerJson.getXp();
        int xpNeeded = levelXp - killerXp;

        if (xpNeeded > 0) return;


        killerJson.setLevel(killerJson.getLevel().getRawNumber() + 1);
        String nextLevel = killerJson.getFormattedLevel(false, true);

        PlayerPackets.title(killer, "&b&lLEVEL UP!",
                currentLevel + " &f➟ " + nextLevel
        );
        int xpReset = 0;
        boolean recursiveLevel = false;

        if (killerXp > levelXp) {
            xpReset = Math.max(killerXp - levelXp,0);
            recursiveLevel = true;
        }

        killerJson.setXp(xpReset);

        if (recursiveLevel)
            handleLevelUp(killer,currentLevel, killerJson);

        tagHandler.refreshPlayerTag(killer);
    }

}
