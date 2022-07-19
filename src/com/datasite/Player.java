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
        while (true) {
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
                wr.write(concat(game.board[id].toString(true), game.board[id ^ 1].toString(false)));
                wr.write("\n");
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
            System.out.println("got x");
            y = sc.nextInt();

            game.board[id ^ 1].hit(x, y);

            try {
                wr.write(concat(game.board[id].toString(true), game.board[id ^ 1].toString(false)));
                wr.write("\n");
                wr.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            game.turn = game.turn ^ 1;
            synchronized (game) {
                game.notify();
            }
        }
    }

    public static String concat(String a, String b) {
        Scanner scA = new Scanner(a);
        Scanner scB = new Scanner(b);

        StringBuilder stringBuilder = new StringBuilder();

        while (scA.hasNextLine()) {
            stringBuilder.append(scA.nextLine());
            stringBuilder.append(scB.nextLine());
        }

        return new String(stringBuilder);
    }
}
