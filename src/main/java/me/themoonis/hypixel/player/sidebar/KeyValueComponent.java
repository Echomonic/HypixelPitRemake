package me.themoonis.hypixel.player.sidebar;

import lombok.RequiredArgsConstructor;
import me.themoonis.hypixel.utils.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.megavex.scoreboardlibrary.api.sidebar.component.LineDrawable;
import net.megavex.scoreboardlibrary.api.sidebar.component.SidebarComponent;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class KeyValueComponent implements SidebarComponent {

    private final String key;
    private final Supplier<String> valueSupplier;

    @Override
    public void draw(@NotNull LineDrawable lineDrawable) {
        String value = valueSupplier.get();

        Component line = Component.text()
                .append(Text.adventure(key))
                .append(Component.text(": "))
                .append(Text.adventure(value).colorIfAbsent(NamedTextColor.AQUA))
                .build();
        lineDrawable.drawLine(line);
    }
}
