package me.themoonis.hypixel.commands.gamemode;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.PermissionCheck;
import dev.echo.concept.api.PlayerAccessible;
import me.themoonis.hypixel.player.enums.PlayerRank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@PlayerAccessible
@Command("gmc")
public class CreativeCommand {

    private void base(CommandSender sender, @Arg("required=false") Player target) {
        if(!(sender instanceof Player)){
            if(target == null){
                sender.sendMessage(ChatColor.RED + "You must supply a target.");
                return;
            }
            Bukkit.dispatchCommand(sender,"gamemode creative " + target.getName());
            return;
        }
        Player wantedTarget = target == null ? (Player) sender : target;
        ((Player) sender).performCommand("gamemode creative " + wantedTarget.getName());
    }

    @PermissionCheck
    private boolean hasPermission(CommandSender sender) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You cannot do this.");
            return false;
        }
        return true;
    }
}
