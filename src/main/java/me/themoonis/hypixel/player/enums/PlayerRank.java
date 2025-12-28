package me.themoonis.hypixel.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;

@RequiredArgsConstructor
public enum PlayerRank {

    DEFAULT("",ChatColor.GRAY),
    VIP("[VIP]",ChatColor.GREEN),
    VIP_PLUS("[VIP&e+%c]", ChatColor.GREEN),
    MVP("[MVP]", ChatColor.AQUA),
    MVP_PLUS("[MVP%s+%c]",ChatColor.AQUA),
    MVP_PLUS_PLUS("[MVP%s++%c]",ChatColor.GOLD),
    YOUTUBER("[&fYOUTUBE%c]",ChatColor.RED),
    ADMIN("[&6ዞ%c]",ChatColor.RED),

    ;

    @Getter
    private final String prefix;
    @Getter
    private final ChatColor color;

    @Override
    public String toString() {
        return toString(ChatColor.RED);
    }

    public String toName(ChatColor plusColor){
        if(this == VIP_PLUS)
            plusColor = ChatColor.YELLOW;

        return getColor() + name().replace("_PLUS",plusColor + "+");
    }

    public String toString(ChatColor plusColor){
        return color + String.format(prefix.replace("%c", color.toString()), plusColor);
    }

}
