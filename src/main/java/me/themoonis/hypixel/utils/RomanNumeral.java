package me.themoonis.hypixel.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RomanNumeral {

  

    public String fromNumber(int number) {
        String[] THOUSANDS = {"", "M", "MM", "MMM"};
        String[] HUNDREDS = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] TENS = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] UNITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};
        return THOUSANDS[number / 1000] +
                HUNDREDS[(number % 1000) / 100] +
                TENS[(number % 100) / 10] +
                UNITS[number % 10];
    }

}
