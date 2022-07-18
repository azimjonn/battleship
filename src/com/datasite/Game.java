package com.datasite;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Game {
    Board[] board;
    int turn;

    public Game() {
        board = new Board[2];
        turn = 1;
    }
}