package me.themoonis.hypixel.player.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.Packet;
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



}
