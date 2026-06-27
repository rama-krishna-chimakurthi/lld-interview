package com.rk.lld;

import java.util.Optional;

import lombok.Getter;

public class Game {

    private Board board;
    private Player player1, player2;

    @Getter
    private GameState gameState;

    @Getter
    private Player winner;

    @Getter
    private Player currentPlayer;

    public Game(Player player1, Player player2) {
        board = new Board();
        this.player1 = player1;
        this.player2 = player2;
        gameState = GameState.IN_PROGRESS;
        winner = null;
        currentPlayer = player1;
    }

    public boolean makeMove(Player player, int col) {
        /*
         * Core Logic:
         * - placeDisc
         * - check if we have winner?
         * - check if it is draw?
         * - swicth current player
         * 
         * Edge Cases:
         * - check whether the currentPlayer is playing the move or not
         * - valid col? -> checked in board
         * - isGameOver?
         */
        if (player != currentPlayer)
            return false;
        if (gameState != GameState.IN_PROGRESS)
            return false;

        Optional<Position> position = board.placeDisc(col, player);
        if (position.isEmpty())
            return false;

        if (board.checkWin(position.get().getRow(), position.get().getCol(), player.getColor())) {
            System.out.println("Game Over!! " + player.getName() + " won");
            gameState = GameState.WIN;
            winner = player;
            return true;
        } else if (board.isFull()) {
            gameState = GameState.DRAW;
            return true;
        }
        switchTurn();
        return true;
    }

    private void switchTurn() {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

}
