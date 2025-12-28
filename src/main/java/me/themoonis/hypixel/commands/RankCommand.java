package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.PlayerAccessible;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.TagHandler;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("rank")
@PlayerAccessible
public class RankCommand {

    private static final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);


    private void base(CommandSender sender, @Arg OfflinePlayer target, @Arg("force-uppercase=true") PlayerRank rank){
        PlayerDataTracker playerTracker = plugin.getPlayerDataTracker();

        if(sender instanceof Player){
            Player player = ((Player) sender);
            if(playerTracker.get(player.getUniqueId()).getRank() != PlayerRank.ADMIN){
                player.sendMessage(Text.color("&cYou must be ADMIN or higher to perform this command."));
                return;
            }
        }

        PlayerJson targetData = playerTracker.getOrCreate(target);
        ChatColor plusColor = ChatColor.valueOf(targetData.getData().computeIfAbsent("plus_color", o -> ChatColor.RED.name()).toString());

        targetData.setRank(rank);
        playerTracker.put(target.getUniqueId(),targetData);

        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            TagHandler tagHandler = plugin.getTagHandler();
            onlineTarget.sendMessage(Text.color("&aYou are now %s&a.", rank.getColor() + rank.toName(plusColor)));
            tagHandler.generatePrefix(onlineTarget);
            tagHandler.displayPlayerTag(onlineTarget);
        }
        sender.sendMessage(Text.color("&aSuccess!"));
    }

}
