package me.themoonis.hypixel.player;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.themoonis.hypixel.player.enums.PlayerRank;
import me.themoonis.hypixel.player.game.PlayerLevel;
import me.themoonis.hypixel.player.game.PlayerPrestige;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Data
public class PlayerJson {

    private final UUID uuid;
    private final String username;
    private PlayerRank rank;

    private PlayerLevel level;
    private PlayerPrestige prestige;

    private int gold;
    private int bounty;
    private int renown;

    private List<String> boughtPerks;
    private List<String> renownPerks;

    private final HashMap<String,Object> data;

    public void setLevel(int rawNumber) {
        level.setRawNumber(rawNumber);
        prestige.setLevel(level);
    }
    public void setPrestige(int rawNumber){
        prestige.setRawNumber(rawNumber);
    }

    public boolean isPrestige(){
        return prestige.getRawNumber() != 0;
    }

    public static PlayerJson fromDomain(PlayerJsonDTO playerJsonDTO){
        PlayerLevel playerLevel = new PlayerLevel(playerJsonDTO.getLevel());
        PlayerPrestige playerPrestige = new PlayerPrestige(playerJsonDTO.getPrestige(),playerLevel);

        return new PlayerJson(
                playerJsonDTO.getUuid(),
                playerJsonDTO.getUsername(),
                playerJsonDTO.getRank(),
                playerLevel,
                playerPrestige,
                playerJsonDTO.getGold(),
                playerJsonDTO.getBounty(),
                playerJsonDTO.getRenown(),
                playerJsonDTO.getBoughtPerks(),
                playerJsonDTO.getRenownPerks(),
                playerJsonDTO.getData()
        );
    }

    public PlayerJsonDTO toDto(){
        return new PlayerJsonDTO(
                uuid,
                username,
                rank,
                level.getRawNumber(),
                prestige.getRawNumber(),
                gold,
                bounty,
                renown,
                boughtPerks,
                renownPerks,
                data
        );
    }
}
