package me.themoonis.hypixel.player.inventories;

import me.themoonis.hypixel.ui.AbstractUserInterface;
import me.themoonis.hypixel.ui.PaginatedUserInterface;
import me.themoonis.hypixel.ui.api.UserInterfaceData;
import me.themoonis.hypixel.ui.button.IUserInterfaceButton;
import me.themoonis.hypixel.ui.button.UserInterfaceButton;
import me.themoonis.hypixel.ui.item.ItemBuilder;
import me.themoonis.hypixel.ui.managers.impl.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TestInventory extends AbstractUserInterface {


    private final PlayerManager playerManager;

    public TestInventory(PlayerManager playerManager) {
        super(playerManager, UserInterfaceData.create(data -> {
            data.addContextObject("title","Test Menu");
            data.addContextObject("size",27);
        }));
        this.playerManager = playerManager;
    }

    @Override
    public void loadItems() {

    }

    @Override
    public void loadButtons() {
        userInterfaceButtons.add(UserInterfaceButton
                .builder()
                        .icon(ItemBuilder.build(Material.GRASS, icon -> {
                            icon.setDisplayName("&aMaterials");
                            icon.setLore(Arrays.asList(
                                    "&7See all the materials in minecraft!",
                                    "",
                                    "&eClick to interact."
                            ));
                        }))
                        .action((viewer, icon, slot) -> {
                            new PaginatedUserInterface(playerManager, UserInterfaceData.create(data -> {
                                data.addContextObject("title","Materials");
                                data.addContextObject("size",54);
                            })){
                                {
                                    click(event -> {
                                        if(event.getCurrentItem() == null) return;
                                        Player player = (Player) event.getWhoClicked();
                                        player.getInventory().addItem(event.getCurrentItem());
                                        event.setCancelled(true);
                                    });

                                    stacks(Arrays.stream(Material.values()).map(ItemStack::new).collect(Collectors.toList()));
                                }

                                @Override
                                protected boolean autoLoad() {
                                    return false;
                                }
                            }.open(viewer);
                        })
                .build());
    }
}
