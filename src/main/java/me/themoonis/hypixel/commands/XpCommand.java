package me.themoonis.hypixel.commands;

import dev.echo.concept.api.*;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.events.PlayerGainXpEvent;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@PlayerOnly
@Command("xp")
public class XpCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(Player sender, @Arg int xp, @Arg("required=false") Player target) {

        Player awarded = (target != null && target.isOnline()) ? target : sender;

        Bukkit.getPluginManager().callEvent(new PlayerGainXpEvent(awarded,xp));

        awarded.playSound(awarded.getLocation(), Sound.SUCCESSFUL_HIT,1,2);
        awarded.sendMessage(Text.pit("UPGRADE","You've been given &b%s XP&7!",ChatColor.GOLD,xp));
        sender.sendMessage(ChatColor.GREEN + "Success!");
    }

    @SubCommand("get")
    private void get(Player sender, @Arg("required=false") Player targetPlayer){
        Player target = (targetPlayer != null && targetPlayer.isOnline()) ? targetPlayer : sender;

        PlayerJson targetJson = plugin.getPlayerDataTracker().get(target.getUniqueId());

        int levelXp = targetJson.isPrestige() ? targetJson.getPrestige().getPrestigeXp() : targetJson.getLevel().getLevelXp();
        int neededXp = levelXp - targetJson.getXp();

        sender.sendMessage(ChatColor.YELLOW + target.getName() + "'s Xp Info");
        sender.sendMessage(ChatColor.GRAY + " Needed Xp: " + ChatColor.AQUA + neededXp);
        sender.sendMessage(ChatColor.GRAY + " Level Xp: " + ChatColor.AQUA + levelXp);
    }

    @PermissionCheck
    private boolean hasPermission(CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (plugin.getPlayerDataTracker().get(player.getUniqueId()).getRank() != PlayerRank.ADMIN) {
            player.sendMessage(ChatColor.RED + "You cannot do this.");
            return false;
        }


        return true;
    }
}
