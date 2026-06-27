package com.rk.lld.connect4;

import lombok.Getter;

@Getter
public class Player {
    private final String name;
    private final DiscColor color;

    Player(String name, DiscColor color) {
        this.name = name;
        this.color = color;
    }
}
