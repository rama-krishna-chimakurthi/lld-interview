package com.rk.lld.connect4;

import lombok.Getter;

@Getter
public class Position {
    private final int row;
    private final int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
