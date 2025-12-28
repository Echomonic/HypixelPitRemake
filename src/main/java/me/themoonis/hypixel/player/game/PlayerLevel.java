package me.themoonis.hypixel.player.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.themoonis.hypixel.player.enums.NeededXp;
import me.themoonis.hypixel.utils.Text;
import org.bukkit.ChatColor;


@AllArgsConstructor
public class PlayerLevel {
    @Getter
    @Setter
    private int rawNumber;

    public ChatColor getColor(){

        return getInfo().getColor();
    }


    public NeededXp getInfo(){
        return NeededXp.valueOf("LVL" + getBaseNumber());
    }

    public String getFormattedText(boolean brackets){
        String levelColor = getColor().toString();
        String levelText = rawNumber >= 50 ? levelColor + ChatColor.BOLD + rawNumber : levelColor + rawNumber;
        String bracketText = Text.color("&7[%s&7]",levelText);

        return brackets ? bracketText : levelText;
    }

    public String getBaseNumber(){
        String levelString = Integer.toString(rawNumber);
        char[] letters = levelString.toCharArray();
        StringBuilder text = new StringBuilder();

        if(rawNumber == 120)
            return "120";

        for(int i = 0; i < letters.length; i++){
            char letter = letters[i];

            if(i == letters.length - 1 && letters[letters.length - 1] != 0)
                letter = '0';

            text.append(letter);
        }
        return text.toString();
    }
}

