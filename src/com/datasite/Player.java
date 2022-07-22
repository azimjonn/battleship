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

    private void printState() {
        try {
            wr.write(concat(game.board[id].toString(true), game.board[id ^ 1].toString(false)));
            wr.write("\n");
            wr.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (game.winner == -1) {
            synchronized (game) {
                while (game.turn != id) {
                    try {
                        game.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

           printState();

            try {
                wr.write("Enter xy: ");
                wr.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int x, y;
            x = sc.nextInt();
            y = sc.nextInt();

            game.board[id ^ 1].hit(x, y);

            printState();

            if (game.board[id ^ 1].isLost()) {
                game.winner = id;
            }

            game.turn = game.turn ^ 1;
            synchronized (game) {
                game.notify();
            }
        }

        synchronized (game) {
            try {
                if (game.winner == id) {
                    wr.write("You won the game.");
                } else {
                    wr.write("You lost the game.");
                }
                wr.flush();
                wr.close();
                sc.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String concat(String a, String b) {
        Scanner scA = new Scanner(a);
        Scanner scB = new Scanner(b);

        StringBuilder stringBuilder = new StringBuilder();

        while (scA.hasNextLine()) {
            stringBuilder.append(scA.nextLine());
            stringBuilder.append("          ");
            stringBuilder.append(scB.nextLine());
            stringBuilder.append('\n');
        }

        return new String(stringBuilder);
    }
}
