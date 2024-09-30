package org.example;

import java.util.Scanner;
import static org.example.Board.BoardProperties.*;

public class Game {
    private final Player player1, player2;
    private final Scanner scan;

    private final String scrollConsole; // to hide board from another player

    public Game() {
        scan = new Scanner(System.in);
        scrollConsole = "\n".repeat(20);
        String name1, name2;

        System.out.println("Player 1, enter your name: ");
        name1 = scan.nextLine();
        System.out.println("Player 2, enter your name: ");
        name2 = scan.nextLine();
        System.out.println("""
                --------------------------------------------
                ------------Welcome to Battleship-----------
                --------------------------------------------
                """);

        System.out.printf("""
                %s place your ships using the following pattern: [a-j][0-9]-[a-j][0-9]
                (E.g, for ship of size 2: a3-b4)
                """, name1);
        player1 = new Player(name1, scan);

        scrollConsole();

        System.out.printf("""
                %s place your ships using the following pattern: [a-j][0-9]-[a-j][0-9]
                (E.g, for ship of size 2: a3-b4)
                """, name2);
        player2 = new Player(name2, scan);

        scrollConsole();
    }

    public void play() {
        System.out.println("""
                --------------------------------------------
                ------------Battleship begins!!!------------
                --------------------------------------------
                """);

        while (true) {
            if (makeMove(player1, player2))
                break;
            if (makeMove(player2, player1))
                break;
        }
        System.out.printf("""
                --------------------------------------------
                ----------------%s you win!-----------------
                --------------------------------------------
                """, player1.getDestroyedShips() == 10 ? player2.getName() : player1.getName());
    }

    /**
     * @return true if game has ended (10 ships destroyed), false otherwise
     */
    private boolean makeMove(Player player, Player enemy) {
        String move;
        while (enemy.getDestroyedShips() != 10) {
            System.out.printf("%s your turn: ", player.getName());
            move = scan.nextLine();

            if (!move.matches("[a-j][0-9]")) {
                System.out.println("Make sure you follow the pattern: [a-j][0-9]");
            } else {
                boolean hitTarget = enemy.receiveHit(yCoords2int.get(move.charAt(0)), Character.getNumericValue(move.charAt(1)));
                printBoards(player);
                if (!hitTarget)
                    break;
            }
        }

        if (enemy.getDestroyedShips() == 10) {
            return true;
        }

        System.out.print("Press Enter to another player make a move");
        scan.nextLine();
        scrollConsole();

        return false;
    }

    private void scrollConsole() {
        System.out.println(scrollConsole);
    }

    private void printBoards(Player player) {
        String[] b1 = player == player1
                ? player1.getBoard(false).split("\n")
                : player2.getBoard(false).split("\n");
        String[] b2 = player == player1
                ? player2.getBoard(true).split("\n")
                : player1.getBoard(true).split("\n");
        combineBoards(b1, b2);
    }

    private void combineBoards(String[] b1, String[] b2) {
        StringBuilder res = new StringBuilder();
        res.append(b1[0]).append("        ").append(b2[0]).append('\n');
        res.append(b1[1]).append("        ").append(b2[1]).append('\n');
        for(int i = 2; i < b1.length; i++) {
            res.append(b1[i]).append("      ").append(b2[i]).append('\n');
        }
        System.out.println(res);
    }
}
