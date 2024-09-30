package org.example;

import lombok.Getter;
import java.util.Scanner;
import static org.example.Board.BoardProperties.*;


public class Player {
    @Getter
    private final String name;
    private final Board board;
    private Ship[] ships;


    public Player(String name, Scanner in) {
        this.name = name;
        board = new Board();
        initShips();
        placeShips(in);
    }

    public boolean receiveHit(int r, int c) {
        return board.receiveHit(r, c);
    }

    public int getDestroyedShips() {
        return board.getDestroyedShips();
    }

    public String getBoard(boolean hideShips) {
        return board.getAsString(hideShips);
    }

    private void initShips() {
        ships = new Ship[10];
        int i = 0;
        for (int shipSize = 5; shipSize >= 2; shipSize--) {
            for (int shipCount = 1; shipCount <= 6 - shipSize; shipCount++) {
                ships[i++] = new Ship(shipSize);
            }
        }
    }

    private void placeShips(Scanner in) {
        String line;
        for (int i = 0; i < ships.length; i++) {
            System.out.printf("Place ship of size %d: %n", ships[i].size);
            line = in.nextLine().trim();
            if (line.matches("[a-j][0-9]-[a-j][0-9]")) {
                if (!board.placeShip(yCoords2int.get(line.charAt(0)), Character.getNumericValue(line.charAt(1)),
                        yCoords2int.get(line.charAt(3)), Character.getNumericValue(line.charAt(4)), ships[i])) {
                    System.out.println("Can't place ship here");
                    i--;
                }
                System.out.println(getBoard(false));
            } else {
                i--;
                System.out.println("Make sure you're following the pattern: [a-j][0-9]-[a-j][0-9]");
            }
        }

        System.out.println(board.getAsString(false));

        System.out.println("Press Enter");
        in.nextLine();
    }
}
