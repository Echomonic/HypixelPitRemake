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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@PlayerAccessible
@Command("prestige")

public class PrestigeCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(CommandSender sender, @Arg OfflinePlayer target, @Arg("required=false") int prestige){
        PlayerDataTracker playerTracker = plugin.getPlayerDataTracker();

        if(sender instanceof Player){
            Player player = ((Player) sender);
            if(playerTracker.get(player.getUniqueId()).getRank() != PlayerRank.ADMIN){
                player.sendMessage(Text.color("&cYou cannot do this."));
                return;
            }
        }

        PlayerJson targetData = playerTracker.getOrCreate(target);
        targetData.setPrestige(prestige);


        if(target.isOnline()) {
            Player onlineTarget = target.getPlayer();
            TagHandler tagHandler = plugin.getTagHandler();
            onlineTarget.sendMessage(Text.pit("UPGRADE", "Your &bprestige &7has been set to %s&7.", ChatColor.GOLD,
                    targetData.getPrestige().getFormattedText(false)));

            tagHandler.generatePrefix(onlineTarget);
            tagHandler.displayPlayerTag(onlineTarget);
        }

        sender.sendMessage(Text.color("&aSuccess!"));

    }
}
