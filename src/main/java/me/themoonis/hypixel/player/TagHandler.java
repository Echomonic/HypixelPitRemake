package me.themoonis.hypixel.player;

import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.player.util.PlayerPackets;
import net.minecraft.server.v1_8_R3.EnumChatFormat;
import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_8_R3.Scoreboard;
import net.minecraft.server.v1_8_R3.ScoreboardTeam;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.scoreboard.CraftScoreboard;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class TagHandler {

    private final HashMap<UUID, ScoreboardTeam> playerTeams;
    private final HypixelPitRemake plugin;

    public TagHandler(HypixelPitRemake plugin) {
        this.plugin = plugin;
        this.playerTeams = new HashMap<>();
    }


    public void displayOtherTags(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().toString().equalsIgnoreCase(onlinePlayer.getUniqueId().toString())) continue;

            ScoreboardTeam playerTeam = playerTeams.get(onlinePlayer.getUniqueId());
            PlayerPackets.send(player, new PacketPlayOutScoreboardTeam(playerTeam, 0));
            PlayerPackets.send(player, new PacketPlayOutScoreboardTeam(playerTeam, 3));
            PlayerPackets.send(player, new PacketPlayOutScoreboardTeam(playerTeam, 2));
        }
    }

    public void displayPlayerTag(Player player) {
        ScoreboardTeam playerTeam = playerTeams.get(player.getUniqueId());

        PlayerPackets.send(new PacketPlayOutScoreboardTeam(playerTeam, 0));
        PlayerPackets.send(new PacketPlayOutScoreboardTeam(playerTeam, 3));
        PlayerPackets.send(new PacketPlayOutScoreboardTeam(playerTeam, 2));
    }

    public void refreshPlayerTag(Player player){
        generatePrefix(player);
        displayPlayerTag(player);
    }

    public void generatePrefix(Player player) {
        if (playerTeams.containsKey(player.getUniqueId()))
            removePlayerTag(player);
        ScoreboardTeam playerTeam = new ScoreboardTeam(getMinecraftBoard(player), player.getUniqueId().toString().replace("-", "").substring(0, 16));
        PlayerJson playerJson = plugin.getPlayerDataTracker().getOrCreate(player);

        String prefix = playerJson.isPrestige() ? playerJson.getPrestige().getFormattedText(false) : playerJson.getLevel().getFormattedText(true);
        prefix += playerJson.getRank().getColor().toString();
        prefix += " ";

        playerTeam.setPrefix(prefix);
        playerTeam.getPlayerNameSet().add(player.getName());

        playerTeams.put(player.getUniqueId(), playerTeam);

        displayPlayerTag(player);
        displayOtherTags(player);
    }

    public void removePlayerTag(Player player) {
        ScoreboardTeam playerTeam = playerTeams.get(player.getUniqueId());
        PlayerPackets.send(new PacketPlayOutScoreboardTeam(playerTeam,1));
        playerTeams.remove(player.getUniqueId());
    }

    private Scoreboard getMinecraftBoard(Player player) {
        if (player.getScoreboard() == null)
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());


        return ((CraftScoreboard) player.getScoreboard()).getHandle();
    }


}
