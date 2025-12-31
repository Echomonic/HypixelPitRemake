package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Command;
import dev.echo.concept.api.PermissionCheck;
import dev.echo.concept.api.PlayerAccessible;
import dev.echo.concept.api.SubCommand;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.inventories.TestInventory;
import me.themoonis.hypixel.ui.managers.impl.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@PlayerAccessible
@Command("test")
public class TestCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(CommandSender sender){

    }
    @SubCommand("menu")
    private void inventory(CommandSender sender){
        if(!(sender instanceof Player)){
            sender.sendMessage(ChatColor.RED + "You must be a player to use this.");
            return;
        }
        Player player = (Player) sender;

        new TestInventory().open(player);
        player.sendMessage(ChatColor.YELLOW + "Opening menu.");
    }


    @PermissionCheck
    private boolean hasPermission(CommandSender sender){
        if(!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if(plugin.getPlayerDataTracker().get(player.getUniqueId()).getRank() != PlayerRank.ADMIN){
            player.sendMessage(ChatColor.RED + "You cannot do this.");
            return false;
        }


        return true;
    }

}
