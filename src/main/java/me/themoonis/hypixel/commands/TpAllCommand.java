package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.PermissionCheck;
import dev.echo.concept.api.PlayerAccessible;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@PlayerAccessible
@Command("tpall")
public class TpAllCommand {

    private void base(CommandSender sender, @Arg("required=false") Player target) {
        if (!(sender instanceof Player)) {
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "You must supply a target.");
                return;
            }
            teleport(sender, target);
            return;
        }
        Player wantedTarget = target == null ? (Player) sender : target;
        teleport(sender, wantedTarget);
    }

    private void teleport(CommandSender sender, Player target) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!player.getName().equalsIgnoreCase(target.getName())) {
                player.teleport(target);
                sender.sendMessage(ChatColor.GREEN + "Teleporting " + player.getName() + "!");
            }
        });
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
