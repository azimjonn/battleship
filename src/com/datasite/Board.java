package com.datasite;

import java.util.Random;

public class Board {
    private Ship navy[];
    private boolean matrix[][];
    
    public Board() {
        navy = new Ship[5];
        matrix = new boolean[10][10];
        for (int i = 0; i < 100; i++)
            matrix[i % 10][i / 10] = true;
        init();
     }

    public void init() {
        Random random = new Random();
        int sizes[] = {5, 4, 3, 3, 2};
        String names[] = {"Carrier", "Battleship", "Destroyer", "Submarine", "Patrol boat"};
        do {
            for (int i = 0; i < 5; i++) {
                int x, y, direction;

                do {
                    x = random.nextInt(10);
                    y = random.nextInt(10);
                    direction = random.nextInt(2);
                } while (!Ship.isValid(sizes[i], x, y, direction));

                navy[i] = new Ship(names[i], sizes[i], x, y, direction);
            }
        } while (!isValid());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("   ");
        for (int i = 0; i < 10; i++)
            stringBuilder.append(i).append("  ");
        stringBuilder.append('\n');
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(i).append("  ");
            for (int j = 0; j < 10; j++) {
                Ship ship = getShip(i, j);
                if (ship == null) {
                    if (matrix[i][j])
                        stringBuilder.append("*  ");
                    else
                        stringBuilder.append("@  ");
                }
                else {
                    if (ship.isAliveXY(i, j))
                        stringBuilder.append("O  ");
                    else
                        stringBuilder.append("X  ");
                }
            }
            stringBuilder.append('\n');
        }

        return new String(stringBuilder);
    }

    Ship getShip(int x, int y) {
        for (int i = 0; i < 5; i++) {
            if (navy[i].contains(x, y)) {
                return navy[i];
            }
        }

        return null;
    }

    public boolean hit(int tx, int ty) {
        for (Ship sh : navy) {
            if (sh.hit(tx, ty)) {
                return true;
            }
        }

        matrix[tx][ty] = false;
        return false;
    }

    public boolean isValid() {
        for (Ship ship1 : navy) {
            for (Ship ship2 : navy) {
                if (ship1 == ship2) continue;

                if (ship1.collides(ship2))
                    return false;
            }
        }

        return true;
    }
}
