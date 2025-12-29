package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.SubCommand;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.map.PitMapLoader;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.function.Consumer;


@Command({"loadmap", "lm"})
public class LoadMapCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(CommandSender sender, @Arg String map) {
        PitMapLoader mapLoader = plugin.getPitMapLoader();

        boolean selectedMap = mapLoader.loadPitMap(map, player ->
                player.kickPlayer(Text.pit("MAP CHANGE", "The map is being changed!", ChatColor.YELLOW)));

        if (selectedMap) {
            sender.sendMessage(ChatColor.GREEN + "Success!");
            sender.sendMessage(ChatColor.YELLOW + "Loading map!");

            plugin.setGameMap(mapLoader.getChosenGameMap());
            plugin.getGameMap().load();
        } else
            sender.sendMessage(ChatColor.RED + "The map could not be found.");
    }

    @SubCommand("list")
    private void list(CommandSender sender) {
        sender.sendMessage(ChatColor.YELLOW + "Available Maps: ");

        for (String directoryName : plugin.getPitMapLoader().getMapDirectoryNames()) {
            sender.sendMessage("     " + ChatColor.AQUA + directoryName);
        }
    }
}
