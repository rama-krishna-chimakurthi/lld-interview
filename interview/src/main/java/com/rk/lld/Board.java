package com.rk.lld;

import java.util.Optional;

import lombok.Getter;

public class Board {
    @Getter
    private static final int rows = 6, cols = 7;
    private static final int winningLength = 4;
    private DiscColor[][] grid;

    public Board() {
        grid = new DiscColor[rows][cols];
    }

    private boolean canPlace(int col) {
        if (col < 0 || col >= getCols())
            return false;
        return grid[0][col] == null;
    }

    public Optional<Position> placeDisc(int col, Player player) {
        /*
         * Core logic:
         * - Check whether it is possible to place a disc there
         * - find the lowest possible row to place the disc
         * - update the color
         * 
         * Edge cases:
         * - col is out of range (checked in canPlace method)
         */
        if (!canPlace(col)) {
            return Optional.empty();
        }

        for (int r = getRows() - 1; r >= 0; r--) {
            if (grid[r][col] == null) {
                grid[r][col] = player.getColor();
                return Optional.of(new Position(r, col));
            }
        }
        return Optional.empty();
    }

    public boolean checkWin(int r, int c, DiscColor color) {

        /*
         * Core Logic:
         * - count all the consecutive colors
         * Edge cases:
         * - check if r and c in bouds
         * - check if color is present in that cell;
         */
        if (!inBounds(r, c) || grid[r][c] != color) {
            return false;
        }

        int[][] DIR = new int[][] {
                { 1, 0 },
                { 0, 1 },
                { 1, 1 },
                { -1, 1 }
        };
        for (int[] d : DIR) {
            int count = 1;
            count += countInDirection(r, c, d[0], d[1], color);
            count += countInDirection(r, c, -d[0], -d[1], color);
            if (count >= winningLength)
                return true;
        }

        return false;
    }

    private int countInDirection(int r, int c, int dr, int dc, DiscColor color) {
        r = r + dr;
        c = c + dc;
        int count = 0;
        while (inBounds(r, c) && grid[r][c] == color) {
            count++;
            r = r + dr;
            c = c + dc;
        }
        return count;
    }

    private boolean inBounds(int r, int c) {
        if (r >= 0 && c >= 0 && r < getRows() && c < getCols())
            return true;
        return false;

    }

    public void printBoard() {
        for (DiscColor[] row : grid) {
            for (DiscColor cell : row) {
                if (cell == DiscColor.RED)
                    System.out.print(" R ");
                else if (cell == DiscColor.YELLOW)
                    System.out.print(" Y ");
                else
                    System.out.print("   ");
            }
            System.out.println();
        }
    }

    public boolean isFull() {
        for (int c = 0; c < getCols(); c++) {
            if (grid[0][c] == null)
                return false;
        }
        return true;
    }
}
