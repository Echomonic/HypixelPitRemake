package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.PermissionCheck;
import dev.echo.concept.api.PlayerAccessible;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@PlayerAccessible
@Command({"gamemode","gm"})
public class GameModeCommand {

    private void base(CommandSender sender,@Arg String gameModeName, @Arg("required=false") Player target) {
        GameMode gameMode = GameMode.valueOf(gameModeName.toUpperCase());

        if(!(sender instanceof Player)){
            if(target == null){
                sender.sendMessage(ChatColor.RED + "You must supply a target.");
                return;
            }
            setGameMode(sender,gameMode,target);
            return;
        }
        Player wantedTarget = target == null ? (Player) sender : target;
        setGameMode(sender,gameMode,wantedTarget);
    }

    private void setGameMode(CommandSender sender, GameMode gameMode, Player target){
        target.setGameMode(gameMode);

        if(sender instanceof Player && sender.getName().equals(target.getName())){
            sender.sendMessage(Text.pit("GAME MODE","Changed your own gamemode to &b&l%s&7.",ChatColor.YELLOW,gameMode.name()));
            return;
        }

        target.sendMessage(Text.pit("GAME MODE","Your gamemode has been chanted to &b&l%s&7.",ChatColor.YELLOW,gameMode.name()));
        sender.sendMessage(Text.pit("GAME MODE","Changed %s gamemode to &b&l%s&7.",ChatColor.YELLOW,target.getName(),gameMode.name()));
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
