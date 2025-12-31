package me.themoonis.hypixel.commands;

import dev.echo.concept.api.Arg;
import dev.echo.concept.api.Command;
import dev.echo.concept.api.PermissionCheck;
import dev.echo.concept.api.PlayerOnly;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.enums.PlayerSetting;
import me.themoonis.hypixel.player.inventories.SettingsInterface;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@PlayerOnly
@Command({"adminsettings", "settings","setting"})
public class AdminSettingCommand {

    private final HypixelPitRemake plugin = HypixelPitRemake.getPlugin(HypixelPitRemake.class);

    private void base(Player player, @Arg("required=false") String setting) {
        PlayerDataTracker tracker = plugin.getPlayerDataTracker();
        PlayerJson playerJson = tracker.get(player.getUniqueId());

        if (setting == null) {
            new SettingsInterface(plugin).open(player);
            player.sendMessage(ChatColor.GREEN + "Opening settings.");
            player.playSound(player.getLocation(), Sound.CLICK, 1, 2);
            return;
        }
        PlayerSetting chosenSetting;

        try {
            chosenSetting = PlayerSetting.valueOf(setting);
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Setting doesn't exist.");
            return;
        }


        List<PlayerSetting> playerSettings = (List<PlayerSetting>) playerJson.getData().computeIfAbsent("admin_settings", s -> new ArrayList<>());

        ChatColor[] textColors = new ChatColor[2];

        if (playerSettings.contains(chosenSetting)) {
            playerSettings.remove(chosenSetting);
            textColors[0] = ChatColor.RED;
            textColors[1] = ChatColor.GRAY;
        }
        else {
            playerSettings.add(chosenSetting);
            textColors[0] = ChatColor.GREEN;
            textColors[1] = ChatColor.YELLOW;
        }

        player.sendMessage(Text.pit("TOGGLED", chosenSetting.getTitle() + textColors[0] + "!", textColors[0], textColors[1]));
        playerJson.getData().put("admin_settings",playerSettings);
        tracker.put(player.getUniqueId(),playerJson);
        player.playSound(player.getLocation(),Sound.SUCCESSFUL_HIT,1,2);
    }

    @PermissionCheck
    private boolean hasPermission(Player player) {
        if (plugin.getPlayerDataTracker().get(player.getUniqueId()).getRank() != PlayerRank.ADMIN) {
            player.sendMessage(ChatColor.RED + "You must be admin or higher to perform this command.");
            return false;
        }
        return true;
    }

}
