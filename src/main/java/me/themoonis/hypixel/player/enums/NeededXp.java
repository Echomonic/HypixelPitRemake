package me.themoonis.hypixel.player.enums;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.HashMap;

public enum NeededXp {

    LVL0(0, 9, 15, ChatColor.GRAY),
    LVL10(10,19,30,ChatColor.BLUE),
    LVL20(20,29,50,ChatColor.DARK_AQUA),
    LVL30(30,39,75,ChatColor.DARK_GREEN),
    LVL40(40,49,125,ChatColor.GREEN),
    LVL50(50,59,300,ChatColor.YELLOW),
    LVL60(60,69,600,ChatColor.GOLD),
    LVL70(70,79,800,ChatColor.RED),
    LVL80(80,89,900,ChatColor.DARK_RED),
    LVL90(90,99,1000,ChatColor.DARK_PURPLE),
    LVL100(100,109,1200,ChatColor.LIGHT_PURPLE),
    LVL110(110,119,1500,ChatColor.WHITE),
    LVL120(120,120,-1,ChatColor.AQUA),


    ;
    @Getter private final int min;
    @Getter private final int max;
    @Getter private final int xp;
    @Getter private final ChatColor color;

    static HashMap<String,String> levelMap = new HashMap<>();
    static {
        int assignedNumber = values().length;
        for(int i = 0; i < values().length; i++){
            assignedNumber--;
            levelMap.put(values()[i].name(),Integer.toString(assignedNumber));

        }
    }
    NeededXp(int min, int max, int xp, ChatColor color) {

        this.min = min;
        this.max = max;
        this.xp = xp;
        this.color = color;
    }

    public static String getAssignedNumber(NeededXp level){


        return levelMap.get(level.name());
    }


}
