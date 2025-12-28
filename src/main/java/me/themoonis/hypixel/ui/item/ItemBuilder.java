package me.themoonis.hypixel.ui.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.SneakyThrows;
import me.themoonis.hypixel.utils.Text;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class ItemBuilder {

    private ItemStack stack;

    public ItemBuilder(Material mat) {
        stack = new ItemStack(mat,1);
    }

    public ItemBuilder(ItemStack stack) {
        this.stack = stack;
    }

    public ItemBuilder(Material mat, int sh) {
        stack = new ItemStack(mat, 1, (byte) sh);
    }

    public static ItemStack build(Material material, Consumer<ItemBuilder> con) {
        ItemBuilder itemBuilder = new ItemBuilder(material);
        con.accept(itemBuilder);

        return itemBuilder.build();
    }

    public static ItemStack build(ItemStack stack, Consumer<ItemBuilder> con) {
        ItemBuilder itemBuilder = new ItemBuilder(stack);
        con.accept(itemBuilder);
        return itemBuilder.build();
    }

    public ItemMeta getItemMeta() {
        return stack.getItemMeta();
    }

    /**
     * @param color, used for setting the color of leather armor
     * @return this
     */
    public ItemBuilder setColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setOwner(Player owner) {
        SkullMeta meta = (SkullMeta) getItemMeta();
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setType(Material material) {
        return this;
    }

    /**
     * @param unbreakable, Just allows the item to not break/have durability.
     * @return this
     */
    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = stack.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDurability(int durability) {
        stack.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setHead(String owner) {
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(Text.color(displayName));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setItemStack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public ItemBuilder setLore(ArrayList<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Text.color(lore));
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Text.color(lore));
        setItemMeta(meta);
        return this;
    }


    public ItemBuilder setLore(String lore) {
        ArrayList<String> loreList = new ArrayList<>();
        loreList.add(lore);
        ItemMeta meta = getItemMeta();
        meta.setLore(Text.color(loreList));
        setItemMeta(meta);
        return this;
    }


    public ItemBuilder addEnchant(Enchantment enchantment, int level) {
        ItemMeta meta = getItemMeta();
        meta.addEnchant(enchantment, level, true);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addUnsafeEnchant(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder setLore(String lore, String split) {
        ItemMeta meta = getItemMeta();
        String[] parts = lore.split(split);
        ArrayList<String> loreList = new ArrayList<>();

        for (String part : parts) {
            loreList.add(Text.color(part));
        }
        meta.setLore(loreList);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... flag) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    @SneakyThrows
    public ItemBuilder setHeadValue(UUID uuid,String value) {
        SkullMeta headMeta = (SkullMeta) getItemMeta();
        if (value.isEmpty())
            return this;

        if(!value.startsWith("http://textures.minecraft.net/texture/"))
            value = "http://textures.minecraft.net/texture/" + value;

        GameProfile profile = new GameProfile(uuid, null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", value).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        setItemMeta(headMeta);
        return this;
    }
    @SneakyThrows
    public ItemBuilder setHeadValue(String value) {
        return setHeadValue(UUID.randomUUID(),value);
    }
    public ItemStack build() {
        return stack;
    }

}
