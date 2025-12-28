package me.themoonis.hypixel.player.util;

import lombok.experimental.UtilityClass;
import me.themoonis.hypixel.utils.Text;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

@UtilityClass
public class PlayerPackets {

    public void send(Player player, Packet<?> packet){
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
    public void send(Packet<?> packet){
        Bukkit.getOnlinePlayers().forEach(player -> send(player,packet));
    }

    public static void title(Player player, String... texts) {
        ((CraftPlayer)player).resetTitle();

        PacketPlayOutTitle title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE,
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Text.color(texts[0]) + "\"}"));

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        if (texts.length == 2 && texts[1] != null) {
            PacketPlayOutTitle subTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
                    IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Text.color(texts[1]) + "\"}"));
            connection.sendPacket(subTitle);
        }

        connection.sendPacket(title);
    }


    public void bar(Player player, String s) {
        PacketPlayOutChat title = new PacketPlayOutChat(
                IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + Text.color(s) + "\"}"), (byte) 2);
        send(player,title);
    }
}
