package me.themoonis.hypixel.player.inventories;

import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.player.PlayerJson;
import me.themoonis.hypixel.player.enums.PlayerSetting;
import me.themoonis.hypixel.ui.AbstractUserInterface;
import me.themoonis.hypixel.ui.api.UserInterfaceData;
import me.themoonis.hypixel.ui.button.UserInterfaceButton;
import me.themoonis.hypixel.ui.item.ItemBuilder;
import me.themoonis.hypixel.ui.managers.impl.PlayerManager;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsInterface extends AbstractUserInterface {


    private final HypixelPitRemake plugin;

    public SettingsInterface(HypixelPitRemake plugin) {
        super(null, UserInterfaceData.create(data -> {
            data.addContextObject("title","Settings");
            data.addContextObject("type", InventoryType.HOPPER);
        }));
        this.plugin = plugin;
    }

    @Override
    protected boolean autoLoad() {
        return false;
    }

    @Override
    public void open(Player player) {
        super.open(player);
        loadSettings(player);
    }

    private void loadSettings(Player player){
        PlayerJson playerJson = plugin.getPlayerDataTracker().get(player.getUniqueId());
        List<PlayerSetting> toggledSettings = (List<PlayerSetting>) playerJson.getData().computeIfAbsent("admin_settings", s -> new ArrayList<>());
        int i = 0;
        for(PlayerSetting setting : PlayerSetting.values()){
            userInterfaceButtons.add(UserInterfaceButton.builder()
                            .slot(i)
                            .icon(generateIcon(setting,toggledSettings.contains(setting)))
                            .action((viewer, item, slot) -> {
                                String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
                                player.performCommand("setting " + name.toUpperCase().replace(" ","_"));
                                open(player);
                            })
                    .build());
            i++;
        }
        
        userInterfaceButtons.updateButtons(this);
    }

    private ItemStack generateIcon(PlayerSetting setting, boolean toggled){
        int blockColor = toggled ? 5 : 14;

        return ItemBuilder.build(Material.STAINED_CLAY, builder -> {
           builder.setDurability(blockColor);
           builder.setDisplayName((toggled ? "&a" : "&c") + setting.getTitle());
           builder.setLore("&7" + WordUtils.wrap(setting.getDescription(),24,"\n&7",false) + "\n\n" +
                   "&eClick to toggle!","\n");
           builder.addItemFlags(ItemFlag.values());
        });
    }


    @Override
    public void loadItems() {

    }
    @Override
    public void loadButtons() {

    }
}
