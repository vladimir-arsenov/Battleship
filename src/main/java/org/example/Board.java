package org.example;

import lombok.Getter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


class Board {
    public static int SIZE = 10;
    public static final String X_COORDINATES = "0  1  2  3  4  5  6  7  8  9";
    public static final String Y_COORDINATES = "abcdefghij";
    public static final Map<Character, Integer> yCoords2int = Y_COORDINATES.chars()
            .mapToObj(c -> (char) c)
            .collect(Collectors.toUnmodifiableMap(letter -> letter, letter -> letter - 97));

    private static final char HIT = 'X';
    private static final char MISS = 'O';
    private static final char SHIP = '#';
    private static final char EMPTY = ' ';
    private final Cell[][] board;
    @Getter
    private int destroyedShips;

    public Board() {
        destroyedShips = 0;
        board = new Cell[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                board[r][c] = new Cell(null);
            }
        }
    }

    public boolean receiveHit(int r, int c) {
        if (r < 0 || r >= SIZE || c < 0 || c >= SIZE) {
            return false;
        }

        if (board[r][c].isHit())
            return true;
        board[r][c].hit();

        if (board[r][c].hasShip()) {
            if (board[r][c].getShip().takeDamage()) {
                destroyedShips++;
                markSurroundingCellsHit(board[r][c].getShip().coords);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean placeShip(int r1, int c1, int r2, int c2, Ship ship) {
        r2 = Integer.max(r1, r2);
        c2 = Integer.max(c1, c2);

        if ( !(r2 == r1 && c2-c1+1 == ship.size || c2 == c1 && r2-r1+1 == ship.size) ) {
            return false;
        }

        Set<int[]> shipCoords = new HashSet<>();
        for (int r = r1; r <= r2; r++) {
            for (int c = c1; c <= c2; c++) {
                if (!board[r][c].isNearShip()) {
                    return false;
                } else {
                    shipCoords.add(new int[] {r, c});
                    board[r][c].setShip(ship);
                }
            }
        }
        markSurroundingCellsNearShip(shipCoords);

        ship.setCoords(shipCoords);

        return true;
    }

    public String getAsString(boolean hideShips) {
        StringBuilder sb = new StringBuilder();
        sb.append(" |").append(X_COORDINATES).append('\n');
        sb.append("------------------------------").append('\n');
        for (int r = 0; r < SIZE; r++) {
            sb.append(Y_COORDINATES.charAt(r)).append('|');
            for (int c = 0; c < SIZE; c++) {
                Cell cell = board[r][c];
                if (cell.isHit() && cell.hasShip()) {
                    sb.append(HIT);
                } else if (cell.isHit()) {
                    sb.append(MISS);
                } else if (!hideShips && board[r][c].hasShip()) {
                    sb.append(SHIP);
                } else {
                    sb.append(EMPTY);
                }
                sb.append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private void markSurroundingCellsHit(Set<int[]> shipCoords) {
        for (int[] coord : shipCoords) {
            int r = coord[0], c = coord[1];

            markCellHit(r+1, c);
            markCellHit(r-1, c);
            markCellHit(r,c+1);
            markCellHit(r,c-1);

            markCellHit(r+1, c+1);
            markCellHit(r-1, c+1);
            markCellHit(r+1,c-1);
            markCellHit(r-1, c-1);
        }
    }

    private void markCellHit(int r, int c) {
        if (r >= 0  && r < SIZE && c >= 0 && c < SIZE) {
            board[r][c].hit();
        }
    }

    private void markSurroundingCellsNearShip(Set<int[]> shipCoords) {
        for (int[] coord : shipCoords) {
            int r = coord[0], c = coord[1];

            markNearShip(r, c);

            markNearShip(r+1, c);
            markNearShip(r-1, c);
            markNearShip(r,c+1);
            markNearShip(r,c-1);

            markNearShip(r+1, c+1);
            markNearShip(r-1, c+1);
            markNearShip(r+1,c-1);
            markNearShip(r-1, c-1);
        }
    }

    private void markNearShip(int r, int c) {
        if (r >= 0  && r < SIZE && c >= 0 && c < SIZE) {
            board[r][c].setNearShip();
        }
    }


}