package me.themoonis.hypixel.commands;

import dev.echo.concept.api.*;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.map.MapConfiguration;
import me.themoonis.hypixel.map.TemporaryGameMap;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.utils.Text;
import me.themoonis.hypixel.utils.WorldPoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

@PlayerOnly
@Command("mapconf")
public class MapConfigurationCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(Player player) {
        player.sendMessage(ChatColor.RED + "Please use one of the sub commands: spawn, leaderboard, npc, save");
    }

    @SubCommand("spawn")
    private void spawn(Player player) {
        TemporaryGameMap gameMap = plugin.getGameMap();
        MapConfiguration mapConfig = gameMap.getMapConfiguration();

        WorldPoint playerPosition = WorldPoint.fromBukkitLocation(player.getLocation());

        mapConfig.setSpawnPoint(playerPosition);

        player.sendMessage(Text.pit("PIT MAP", "&aSuccessfully &7updated &espawn point &7position! &8(%s)", ChatColor.DARK_AQUA, playerPosition));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
    }

    @SubCommand("center")
    private void center(Player player) {
        Location playerLocation = player.getLocation();
        float yaw = playerLocation.getYaw();
        BlockFace blockFace = WorldPoint.getCardinalDirection(yaw);

        switch (blockFace) {
            case NORTH:
                yaw = 180;
            case SOUTH:
                yaw = 0;
            case EAST:
                yaw = -90;
            case WEST:
                yaw = 90;
        }
        Location centeredPlayerLocation = new Location(playerLocation.getWorld(),
                playerLocation.getBlockX() + .5,
                playerLocation.getY(),
                playerLocation.getBlockZ() + .5,
                yaw,
                0);


        player.teleport(centeredPlayerLocation);

        player.sendMessage(ChatColor.GREEN + "Success!");
        player.playSound(centeredPlayerLocation,Sound.CLICK,1,2);
    }

    @SubCommand("leaderboard")
    private void leaderboard(Player player) {
        TemporaryGameMap gameMap = plugin.getGameMap();
        MapConfiguration mapConfig = gameMap.getMapConfiguration();

        WorldPoint playerPosition = WorldPoint.fromBukkitLocation(player.getLocation());
        mapConfig.setLeaderboardPoint(playerPosition);
        player.sendMessage(Text.pit("PIT MAP", "&aSuccessfully &7updated &eleaderboard &7position! &8(%s)", ChatColor.DARK_AQUA, playerPosition));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
    }

    @SubCommand("npc")
    private void npc(Player player, @Arg String npcId, @Arg("required=false") boolean remove) {
        TemporaryGameMap gameMap = plugin.getGameMap();
        MapConfiguration mapConfig = gameMap.getMapConfiguration();

        npcId = npcId.toLowerCase().trim().replace(" ", "_");

        HashMap<String, WorldPoint> npcPoints = mapConfig.getNpcPoints();

        WorldPoint playerPosition = WorldPoint.fromBukkitLocation(player.getLocation());

        if (remove) {
            if (!npcPoints.containsKey(npcId)) {
                player.sendMessage(ChatColor.RED + "There is no NPC named " + npcId + ".");
                return;
            }
            npcPoints.remove(npcId);

            player.sendMessage(Text.pit("PIT MAP", "&cRemoved &e%s &7from NPC points.", ChatColor.DARK_AQUA, npcId));
            player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
            return;
        }

        if (npcPoints.containsKey(npcId))
            player.sendMessage(Text.pit("PIT MAP", "&aSuccessfully &7updated &e%s's &7position! &8(%s)", ChatColor.DARK_AQUA, npcId, playerPosition));
        else
            player.sendMessage(Text.pit("PIT MAP", "&aSuccessfully &7set &e%s &7position! &8(%s)", ChatColor.DARK_AQUA, npcId, playerPosition));

        npcPoints.put(npcId, playerPosition);
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
    }

    @SubCommand("save")
    private void save(Player player) {
        TemporaryGameMap gameMap = plugin.getGameMap();
        gameMap.saveConfiguration();
        ;
        player.sendMessage(Text.pit("PIT MAP", "&aSuccessfully &7saved configuration!", ChatColor.DARK_AQUA));
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 2);
    }

    @PermissionCheck
    private boolean isAdminCheck(Player player) {
        PlayerDataTracker playerTracker = plugin.getPlayerDataTracker();

        if (playerTracker.get(player.getUniqueId()).getRank() != PlayerRank.ADMIN) {
            player.sendMessage(Text.color("&cYou must be ADMIN or higher to perform this command."));
            return false;
        }

        return true;
    }


}
