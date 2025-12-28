package me.themoonis.hypixel.ui;

import me.themoonis.hypixel.ui.api.UserInterfaceData;
import me.themoonis.hypixel.ui.button.UserInterfaceButton;
import me.themoonis.hypixel.ui.item.ItemBuilder;
import me.themoonis.hypixel.ui.managers.impl.PlayerManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class PaginatedUserInterface extends AbstractUserInterface {

    private List<ItemStack> stacks = new ArrayList<>();

    private int page, index;
    protected int maxItemsPerPage = -1;

    private int leftSlot, rightSlot;

    protected PaginatedUserInterface(PlayerManager playerManager, UserInterfaceData<?> data) {
        super(playerManager, data);

        int size = getInventory().getSize();
        int slotIndex = (size - 9) / 9;

        leftSlot = 3 + (9 * slotIndex);
        rightSlot = 5 + (9 * slotIndex);
    }

    protected void stacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    @Override
    public void open(Player player) {
        this.userInterfaceActions.clear();
        this.userInterfaceButtons.clear();

        this.miscLoad();
        //uiButtonHandler.updateButtons(this);
        super.open(player);
    }

    @Override
    public void loadItems() {
        if (stacks == null) return;
        if (stacks.isEmpty()) return;

        if (maxItemsPerPage == -1)
            maxItemsPerPage = Math.toIntExact(Arrays.stream(getInventory().getContents()).filter(stack -> stack == null || stack.getType() == Material.AIR).count());

        for (int i = 0; i < maxItemsPerPage; i++) {
            index = maxItemsPerPage * page + i;

            if (index >= stacks.size()) break;
            if (stacks.get(index) == null) continue;

            userInterfaceActions.addItem(stacks.get(index));
        }
    }

    //TODO: Fix this paging system .
    @Override
    public void loadButtons() {
        if (stacks == null) return;
        if (page > 0)
            userInterfaceButtons.add(UserInterfaceButton.builder().icon(ItemBuilder.build(Material.ARROW, builder -> {
                builder.setDisplayName("<green><--");
                builder.setLore("<gray>Go back a page");
            })).action((viewer, item, slot) -> {
                if (page - 1 >= 0)
                    page -= 1;


                viewer.playSound(viewer.getLocation(), Sound.CLICK,1,2f);

                open(viewer);
            }).slot(leftSlot).build());

        if (stacks.isEmpty()) return;

        if (!(index > stacks.size()))
            userInterfaceButtons.add(UserInterfaceButton.builder().icon(ItemBuilder.build(Material.ARROW, builder -> {
                builder.setDisplayName("<green>-->");
                builder.setLore("<gray>Go forward a page");
            })).action((viewer, item, slot) -> {
                page += 1;
                viewer.playSound(viewer.getLocation(), Sound.CLICK,1,2f);
                open(viewer);
            }).slot(rightSlot).build());
    }

    protected void clearPageItems() {
        if (stacks == null) return;
        this.stacks.clear();
    }

    protected void forceSetPage(int page) {
        this.page = page;
    }
}
