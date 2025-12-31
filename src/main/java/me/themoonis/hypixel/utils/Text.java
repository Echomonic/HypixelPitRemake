package me.themoonis.hypixel.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class Text {

    public String color(String text, Object... args){
        return ChatColor.translateAlternateColorCodes('&', String.format(text, args));
    }
    public List<String> color(List<String> strings){

        return strings.stream().map(Text::color).collect(Collectors.toList());
    }

    public static Component adventure(String text){
        return Component.text(color(text));
    }

    public String pit(String subject, String text, ChatColor subjectColor, Object... args){
        return pit(subject,text,subjectColor,ChatColor.GRAY,args);
    }
    public String pit(String subject, String text, ChatColor subjectColor,ChatColor textColor, Object... args){
        return color("%s&l%s! &r%s%s",subjectColor,subject.toUpperCase(), textColor,String.format(text, args));
    }
}
