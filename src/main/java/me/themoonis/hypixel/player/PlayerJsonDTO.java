package me.themoonis.hypixel.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import me.themoonis.hypixel.player.enums.PlayerRank;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class PlayerJsonDTO {

    private final UUID uuid;
    private final String username;
    private final PlayerRank rank;

    private int level;
    private int prestige;

    private int xp;

    private double gold;
    private int bounty;
    private int renown;

    private List<String> boughtPerks;
    private List<String> renownPerks;

    private final HashMap<String,Object> data;


}
