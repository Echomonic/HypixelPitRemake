package me.themoonis.hypixel;

import dev.echo.concept.api.ArgumentParser;
import dev.echo.concept.impl.CommandRegistry;
import dev.echo.concept.impl.ParserRegistry;
import lombok.Getter;
import lombok.Setter;
import me.themoonis.hypixel.commands.*;
import me.themoonis.hypixel.commands.parsers.ObjectParser;
import me.themoonis.hypixel.commands.parsers.PlayerRankParser;
import me.themoonis.hypixel.json.trackers.PlayerDataTracker;
import me.themoonis.hypixel.listeners.BlockListener;
import me.themoonis.hypixel.listeners.PlayerListener;
import me.themoonis.hypixel.map.PitMapLoader;
import me.themoonis.hypixel.map.TemporaryGameMap;
import me.themoonis.hypixel.player.TagHandler;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.sidebar.SidebarHandler;
import me.themoonis.hypixel.ui.listener.InventoryListener;
import me.themoonis.hypixel.utils.Text;
import net.megavex.scoreboardlibrary.api.ScoreboardLibrary;
import net.megavex.scoreboardlibrary.api.exception.NoPacketAdapterAvailableException;
import net.megavex.scoreboardlibrary.api.noop.NoopScoreboardLibrary;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public final class HypixelPitRemake extends JavaPlugin {

    public static final boolean DEBUG = true;

    @Getter
    private PlayerDataTracker playerDataTracker;

    @Getter
    @Setter
    private TemporaryGameMap gameMap;

    @Getter
    private PitMapLoader pitMapLoader;

    @Getter
    private TagHandler tagHandler;

    @Getter
    private SidebarHandler sidebarHandler;

    @Getter
    private ScoreboardLibrary scoreboardLibrary;

    private ParserRegistry parserRegistry;

    @Override
    public void onEnable() {
        this.playerDataTracker = new PlayerDataTracker(getDataFolder().toPath());
        this.sidebarHandler = new SidebarHandler();

        try{
            scoreboardLibrary = ScoreboardLibrary.loadScoreboardLibrary(this);
        }catch (NoPacketAdapterAvailableException e){
            scoreboardLibrary = new NoopScoreboardLibrary();
        }
        pitMapLoader = new PitMapLoader(Bukkit.getWorldContainer().toPath().resolve("Pit Maps"));
        pitMapLoader.choose();

        gameMap = pitMapLoader.getChosenGameMap();
        gameMap.load();

        tagHandler = new TagHandler(this);

        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        Bukkit.getPluginManager().registerEvents(new BlockListener(this),this);
        Bukkit.getConsoleSender().sendMessage(Text.pit("HYPIXEL PIT", "&aSuccessfully &7loaded up plugin.", ChatColor.YELLOW));

        parserRegistry = registerParsers();
        CommandRegistry commandRegistry = new CommandRegistry(parserRegistry);
        Class<?>[] commandClasses = new Class[]{
                RankCommand.class,
                LevelCommand.class,
                PrestigeCommand.class,
                LoadMapCommand.class,
                MapConfigurationCommand.class,
                TestCommand.class,
                XpCommand.class,
                AdminSettingCommand.class
        };

        for (Class<?> commandClass : commandClasses)
            commandRegistry.register("hypixelcommand", commandClass);

        Bukkit.getOnlinePlayers().forEach(player -> {
            playerDataTracker.getOrCreate(player);

            if (DEBUG)
                Bukkit.getConsoleSender().sendMessage(
                        Text.pit("HYPIXEL PIT DEBUG",
                                "&aLoaded &7player data for &3%s&7.",
                                ChatColor.AQUA, player.getName()));
        });
        sidebarHandler.start(this);
    }

    @Override
    public void onDisable() {
        scoreboardLibrary.close();
        sidebarHandler.clear();

        if (parserRegistry != null)
            parserRegistry.unload();

        gameMap.unload();
        gameMap = null;

        Bukkit.getOnlinePlayers().forEach(this::saveOnlinePlayer);
        Bukkit.getConsoleSender().sendMessage(Text.pit("HYPIXEL PIT", "&aSuccessfully &7unloaded up plugin.", ChatColor.YELLOW));

        playerDataTracker.flush();
    }

    private void saveOnlinePlayer(Player player) {
        playerDataTracker.put(player.getUniqueId(), playerDataTracker.get(player.getUniqueId()));

        if (DEBUG)
            Bukkit.getConsoleSender().sendMessage(
                    Text.pit("HYPIXEL PIT DEBUG",
                            "&aSaved &7player data for &3%s&7.",
                            ChatColor.AQUA, player.getName()));
    }

    private ParserRegistry registerParsers() {
        ParserRegistry parserRegistry = new ParserRegistry();

        parserRegistry.add(PlayerRank.class, new PlayerRankParser());
        parserRegistry.add(Object.class, new ObjectParser());

        parserRegistry.load();

        parserRegistry.add(Player.class, (ArgumentParser<Player>) (s, argContext) -> {
            boolean useUUID = argContext.getSettingOr("search", "name", String.class).equalsIgnoreCase("uuid");
            return useUUID ? Bukkit.getPlayer(UUID.fromString(s)) : Bukkit.getPlayer(s);
        });

        parserRegistry.add(OfflinePlayer.class, (ArgumentParser<OfflinePlayer>) (s, argContext) -> {
            boolean useUUID = argContext.getSettingOr("search", "name", String.class).equalsIgnoreCase("uuid");
            return useUUID ? Bukkit.getOfflinePlayer(UUID.fromString(s)) : Bukkit.getOfflinePlayer(s);
        });

        return parserRegistry;
    }

}
