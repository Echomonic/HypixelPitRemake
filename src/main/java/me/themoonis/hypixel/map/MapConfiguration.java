package me.themoonis.hypixel.map;

import lombok.*;
import me.themoonis.hypixel.utils.WorldPoint;
import org.bukkit.util.Vector;

import java.util.HashMap;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MapConfiguration {

    private WorldPoint spawnPoint;

    private WorldPoint leaderboardPoint;

    private HashMap<String,WorldPoint> npcPoints;

}
