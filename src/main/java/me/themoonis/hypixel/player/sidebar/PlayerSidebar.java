package me.themoonis.hypixel.player.sidebar;

import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.HypixelPitRemake;
import me.themoonis.hypixel.player.PlayerJson;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.megavex.scoreboardlibrary.api.sidebar.Sidebar;
import net.megavex.scoreboardlibrary.api.sidebar.component.ComponentSidebarLayout;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PlayerSidebar {

    private final Sidebar sidebar;
    private final ComponentSidebarLayout componentSidebarLayout;

    public PlayerSidebar(PlayerJson data, Sidebar sidebar){
        this.sidebar = sidebar;

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");

        SidebarComponent title = SidebarComponent.staticLine(Component.text("THE HYPIXEL PIT", NamedTextColor.YELLOW).decorate(TextDecoration.BOLD));


        SidebarComponent level = new KeyValueComponent("Level", () -> data.getFormattedLevel(false,true));
        SidebarComponent neededXpComponent = new KeyValueComponent("Needed XP", () -> {
            if(data.getLevel().getRawNumber() == 120)
                return "&b&lMAXED!";

            int levelXp = data.isPrestige() ? data.getPrestige().getPrestigeXp() : data.getLevel().getLevelXp();
            return NumberFormat.getNumberInstance().format(levelXp - data.getXp());
        });
        SidebarComponent gold = new KeyValueComponent("Gold", () -> "&6" + NumberFormat.getNumberInstance().format(data.getGold()) + "g");
        SidebarComponent playerStatus = new KeyValueComponent("Status", () -> "&aIdling");

        SidebarComponent lines = SidebarComponent
                .builder()
                .addDynamicLine(() -> {
                    String date = dateFormat.format(new Date());
                    return Component.text(date,NamedTextColor.GRAY);
                })
                .addBlankLine()
                .addComponent(level)
                .addComponent(neededXpComponent)
                .addBlankLine()
                .addComponent(gold)
                .addBlankLine()
                .addComponent(playerStatus)
                .addBlankLine()
                .addStaticLine(Component.text("www.hypixel.net",NamedTextColor.YELLOW))
                .build();

        this.componentSidebarLayout = new ComponentSidebarLayout(title,lines);
    }

    public void close(){
        sidebar.close();
    }

    public void tick(){

        componentSidebarLayout.apply(sidebar);
    }
}
