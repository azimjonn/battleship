package com.datasite;

import javax.swing.plaf.TableHeaderUI;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
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
            Player player1 = new Player(game, System.in, System.out, 1);

            ServerSocket serverSocket = new ServerSocket(1024);
            System.out.println("Waiting for other person to connect...");
            Socket socket = serverSocket.accept();

            System.out.println("Other person successfully connected.");

            Player player2 = new Player(game, socket.getInputStream(), socket.getOutputStream(), 2);

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
            System.out.println(System.in.transferTo(socket.getOutputStream()));
            socket.getInputStream().transferTo(System.out);


            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
