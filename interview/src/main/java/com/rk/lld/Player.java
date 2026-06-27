package com.rk.lld;

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
