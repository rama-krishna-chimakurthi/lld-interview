package com.rk.lld.connect4;

import java.util.Optional;

public class Board {

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int WINNING_LENGTH = 4;

    private static final int[][] DIRECTIONS = {
            { 1, 0 },
            { 0, 1 },
            { 1, 1 },
            { -1, 1 }
    };

    private final DiscColor[][] grid;

    /*
     * Core Logic:
     * - Create an empty board.
     *
     * Edge Cases:
     * - None.
     */
    public Board() {
        grid = new DiscColor[ROWS][COLS];
    }

    /*
     * Core Logic:
     * - Check whether a disc can be placed in the given column.
     * - Find the lowest available row.
     * - Place the player's disc.
     * - Return the position where the disc was placed.
     *
     * Edge Cases:
     * - Column index out of bounds.
     * - Column is already full.
     */
    public Optional<Position> placeDisc(int column, DiscColor color) {
        if (!canPlace(column))
            return Optional.empty();

        for (int row = ROWS - 1; row >= 0; row--) {
            if (grid[row][column] == null) {
                grid[row][column] = color;
                return Optional.of(new Position(row, column));
            }
        }

        return Optional.empty();
    }

    /*
     * Core Logic:
     * - Verify the given position contains the expected disc.
     * - Check all four possible directions:
     * Vertical
     * Horizontal
     * Main Diagonal
     * Anti-Diagonal
     * - Count consecutive discs in both directions.
     * - Return true if any direction has WINNING_LENGTH discs.
     *
     * Edge Cases:
     * - Position outside board.
     * - Empty cell.
     * - Cell contains a different color.
     * - Winning sequence touching board boundaries.
     */
    public boolean checkWin(int row, int col, DiscColor color) {

        if (!inBounds(row, col) || grid[row][col] != color)
            return false;

        for (int[] direction : DIRECTIONS) {
            int count = 1;
            count += countInDirection(row, col,
                    direction[0], direction[1], color);
            count += countInDirection(row, col,
                    -direction[0], -direction[1], color);
            if (count >= WINNING_LENGTH)
                return true;
        }

        return false;
    }

    /*
     * Core Logic:
     * - Move one step in the given direction.
     * - Continue while the same color is found.
     * - Count consecutive discs.
     *
     * Edge Cases:
     * - Immediately reaches board boundary.
     * - Adjacent cell has different color.
     * - No consecutive discs.
     */
    private int countInDirection(
            int row,
            int col,
            int rowDelta,
            int colDelta,
            DiscColor color) {

        int count = 0;

        row += rowDelta;
        col += colDelta;

        while (inBounds(row, col)
                && grid[row][col] == color) {

            count++;

            row += rowDelta;
            col += colDelta;
        }
        return count;
    }

    /*
     * Core Logic:
     * - Verify the column is valid.
     * - Check whether the top cell is empty.
     *
     * Edge Cases:
     * - Negative column.
     * - Column exceeds board width.
     * - Column is completely filled.
     */
    private boolean canPlace(int column) {

        return column >= 0 &&
                column < COLS &&
                grid[0][column] == null;
    }

    /*
     * Core Logic:
     * - Verify row and column lie within the board.
     *
     * Edge Cases:
     * - Negative indices.
     * - Indices equal to board dimensions.
     */
    private boolean inBounds(int row, int col) {

        return row >= 0 &&
                row < ROWS &&
                col >= 0 &&
                col < COLS;
    }

    /*
     * Core Logic:
     * - Check every column's top cell.
     * - If any top cell is empty,
     * the board is not full.
     *
     * Edge Cases:
     * - Empty board.
     * - Completely filled board.
     */
    public boolean isFull() {

        for (int col = 0; col < COLS; col++) {

            if (grid[0][col] == null)
                return false;
        }

        return true;
    }

    /*
     * Core Logic:
     * - Return the disc at the given position.
     *
     * Edge Cases:
     * - Caller should ensure indices are valid.
     */
    public DiscColor getCell(int row, int col) {
        return grid[row][col];
    }
}