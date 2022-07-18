package com.datasite;

import java.io.*;
import java.util.Scanner;

public class Player extends Thread{
    Game game;
    Scanner sc;
    Writer wr;

    OutputStream outputStream;
    int id;
    
    public Player(Game game, InputStream inputStream, OutputStream outputStream, int id) {
        this.game = game;
        this.sc = new Scanner(inputStream);
        this.wr = new OutputStreamWriter(outputStream);
        this.outputStream = outputStream;
        this.id = id;
    }

    @Override
    public void run() {
        synchronized (game) {
            while (game.turn != id) {
                try {
                    game.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            wr.write(game.board[0].toString());
            wr.write("\n");
            wr.write(game.board[1].toString());
            wr.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            wr.write("Enter xy: ");
            wr.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int x, y;
        x = sc.nextInt();
        y = sc.nextInt();

        game.board[id - 1].hit(x, y);

        try {
            wr.write(game.board[0].toString());
            wr.write("\n");
            wr.write(game.board[1].toString());
            wr.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        game.turn = game.turn == 1 ? 2 : 1;
        game.notify();
    }
}
