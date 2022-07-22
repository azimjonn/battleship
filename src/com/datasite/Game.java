package com.datasite;

class Game {
    Board[] board;
    int winner;
    int turn;

    public Game() {
        board = new Board[2];
        board[0] = new Board();
        board[1] = new Board();
        winner = -1;
        turn = 0;
    }
}