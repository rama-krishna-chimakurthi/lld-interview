package com.rk.lld;

import java.util.Optional;

import lombok.Getter;

public class Game {

    private final Board board;
    private final Player player1;
    private final Player player2;

    @Getter
    private GameState gameState;

    @Getter
    private Player winner;

    @Getter
    private Player currentPlayer;

    /*
     * Core Logic:
     * - Validate players.
     * - Initialize the board.
     * - Set the first player.
     * - Start the game in IN_PROGRESS state.
     *
     * Edge Cases:
     * - Null players.
     * - Same player object.
     * - Players with identical disc colors.
     */
    public Game(Player player1, Player player2) {

        if (player1 == null || player2 == null)
            throw new IllegalArgumentException("Players cannot be null");

        if (player1 == player2)
            throw new IllegalArgumentException("Players must be different");

        if (player1.getColor() == player2.getColor())
            throw new IllegalArgumentException("Players must have different colors");

        this.player1 = player1;
        this.player2 = player2;

        this.board = new Board();
        this.currentPlayer = player1;
        this.gameState = GameState.IN_PROGRESS;
    }

    /*
     * Core Logic:
     * 1. Validate whether the move is allowed.
     * 2. Place the disc on the board.
     * 3. Update the game state (Win / Draw).
     * 4. Switch the turn if the game is still in progress.
     *
     * Edge Cases:
     * - Move after game is over.
     * - Wrong player tries to play.
     * - Invalid column.
     * - Column is already full.
     */
    public boolean makeMove(Player player, int column) {

        if (!validateMove(player))
            return false;

        Optional<Position> position = board.placeDisc(column, player.getColor());

        if (position.isEmpty())
            return false;

        updateGameState(position.get(), player);

        if (gameState == GameState.IN_PROGRESS)
            switchTurn();

        return true;
    }

    /*
     * Core Logic:
     * - Ensure the game is still in progress.
     * - Ensure the current player is making the move.
     *
     * Edge Cases:
     * - Null player.
     * - Game already finished.
     * - Player plays twice consecutively.
     */
    private boolean validateMove(Player player) {
        return currentPlayer == player &&
                gameState == GameState.IN_PROGRESS;
    }

    /*
     * Core Logic:
     * - Check whether the latest move creates a winning sequence.
     * - If not, check whether the board is completely filled.
     * - Update winner and game state accordingly.
     *
     * Edge Cases:
     * - Winning move on the last available cell
     * (Win takes precedence over Draw).
     * - Invalid position (handled by Board).
     */
    private void updateGameState(Position position, Player player) {

        if (board.checkWin(position.getRow(),
                position.getCol(),
                player.getColor())) {

            gameState = GameState.WIN;
            winner = player;
            return;
        }

        if (board.isFull()) {
            gameState = GameState.DRAW;
        }
    }

    /*
     * Core Logic:
     * - Toggle the current player.
     *
     * Edge Cases:
     * - Should only be called when the game
     * is still in progress.
     */
    private void switchTurn() {
        currentPlayer = currentPlayer == player1
                ? player2
                : player1;
    }
}