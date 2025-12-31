package me.themoonis.hypixel.player.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PlayerSetting {

    BUILD_MODE("Build Mode","Break & place anything you want!"),

    ;

    private final String title;
    private final String description;

}
