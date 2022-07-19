package com.datasite;

import javax.swing.plaf.TableHeaderUI;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        int option;
        do {
            System.out.println("Choose option:\n" +
                    "1. New game\n" +
                    "2. Connect to the game\n");

            option = sc.nextInt();

            if (option != 1 && option != 2)
                System.out.println("Invalid option.");
        } while (option != 1 && option != 2);

        if (option == 1) {
            Game game = new Game();
            Player player1 = new Player(game, System.in, System.out, 0);

            ServerSocket serverSocket = new ServerSocket(1024);
            System.out.println("Waiting for other person to connect...");
            Socket socket = serverSocket.accept();

            System.out.println("Other person successfully connected.");

            Player player2 = new Player(game, socket.getInputStream(), socket.getOutputStream(), 1);

            player1.start();
            player2.start();

            synchronized (game) {
                game.notifyAll();
            }
        } else {
            System.out.print("Enter local server IP address: ");
            String ip = sc.next();

            System.out.println("Trying to connect...");
            Socket socket = new Socket(ip, 1024);
            System.out.println("Successfully connected to the server.");

            new Thread() {
                @Override
                public void run() {
                    try {
                        socket.getInputStream().transferTo(System.out);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }.start();

            OutputStream outputStream = socket.getOutputStream();
            System.out.println("56");
            while (true) {
                outputStream.write(System.in.read());
                System.out.println("58");
                outputStream.flush();
            }
        }
    }
}
