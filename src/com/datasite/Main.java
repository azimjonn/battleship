package com.datasite;

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
            Socket socket = serverSocket.accept();

            Player player2 = new Player(game, socket.getInputStream(), socket.getOutputStream(), 2);

            player1.start();
            player2.start();

            game.notifyAll();
        } else {
            System.out.print("Enter local server IP address: ");
            String ip = sc.next();

            Socket socket = new Socket(ip, 1024);
            System.setIn(socket.getInputStream());
            System.setOut((PrintStream) socket.getOutputStream());
        }
    }
}
