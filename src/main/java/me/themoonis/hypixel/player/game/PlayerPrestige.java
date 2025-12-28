package me.themoonis.hypixel.player.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.themoonis.hypixel.player.enums.Prestiges;
import me.themoonis.hypixel.utils.RomanNumeral;
import org.bukkit.ChatColor;

public class PlayerPrestige {


    @Getter
    private int rawNumber;
    @Getter
    @Setter
    private PlayerLevel level;

    private String romanNumeral;

    private static final int MAX_PRESTIGE = 50;

    public PlayerPrestige(int rawNumber, PlayerLevel level) {
        this.rawNumber = Math.min(rawNumber,MAX_PRESTIGE);
        this.level = level;
        this.romanNumeral = RomanNumeral.fromNumber(rawNumber);
    }

    public int getPrestigeXp() {
        if (getPrestigeInfo().getXpArray() == null) return -1;
        XpValue value = XpValue.valueOf("LVL" + level.getBaseNumber());
        return getPrestigeInfo().getXpArray()[value.integerFromArray];
    }

    public ChatColor getBracketColor() {
        return getPrestigeInfo().getColor();
    }

    public void setRawNumber(int rawNumber) {
        this.rawNumber = rawNumber;
        this.romanNumeral = RomanNumeral.fromNumber(rawNumber);
    }

    public String getFormattedText(boolean dash) {
        ChatColor bracketColor = getBracketColor();
        String bracketText = "%s[%s%s]";

        String levelText = level.getFormattedText(false);
        String prestigeText = ChatColor.YELLOW + romanNumeral + getBracketColor() + "-";


        return String.format(bracketText, bracketColor,
                (dash ? prestigeText + levelText : levelText),
                bracketColor);
    }

    public Prestiges getPrestigeInfo() {
        return Prestiges.valueOf(formatPrestige());
    }

    private String formatPrestige() {
        return "PRES" + rawNumber;
    }

    @RequiredArgsConstructor
    public enum XpValue {

        LVL0(0),
        LVL10(1),
        LVL20(2),
        LVL30(3),
        LVL40(4),
        LVL50(5),
        LVL60(6),
        LVL70(7),
        LVL80(8),
        LVL90(9),
        LVL100(10),
        LVL110(11),
        LVL120(12),
        ;

        private final int integerFromArray;
    }
}
