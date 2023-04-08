package battleship;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class Battlefield {
    private final int gameFieldSize;  // max size would be 26 because that many letters in the alphabet
    private final String[][] gameField;
    private static final LinkedHashMap<String, Integer> ships = new LinkedHashMap<>();
    static {
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);
    }

    public Battlefield(int size) {
        this.gameFieldSize = size;
        this.gameField = new String[size + 1][size + 1];
        for (int i = 0; i < size + 1; ++i) {
            for (int j = 0; j < size + 1; ++j) {
                if (i == 0) {
                    if (j == 0) this.gameField[i][j] = " ";
                    else this.gameField[i][j] = String.valueOf(j);
                } else {
                    if (j == 0) this.gameField[i][j] = String.valueOf((char) ('A' + i - 1));
                    else this.gameField[i][j] = "~";
                }
            }
        }
    }

    public void startGame() {
        addShips();
    }

    // Add ships
    private void addShips() {
        Scanner scanner = new Scanner(System.in);
        for (String key : ships.keySet()) {
            System.out.println("Enter the coordinates of the " + key + " (" + ships.get(key) + " cells):");
            while (true) {
                System.out.print("> ");
                String answer = scanner.nextLine();
                if (checkCoordinates(answer)) break;
            }
        }
    }

    private boolean checkCoordinates(String coordinates) {
        // Check if coordinates are in correct format
        if (!(coordinates.matches("^[A-Z]([1-9]|1\\d|2[0-6])\\s[A-Z]([1-9]|1\\d|2[0-6])$"))) {
            System.out.println("Error 1! Wrong ship location! Try again:");
            return false;
        }
        // Check if coordinates fit in the game field
        Scanner scn = new Scanner(coordinates);
        while (scn.hasNext()) {
            String next = scn.next();
            if (next.charAt(0) - 'A' >= this.gameFieldSize) {
                System.out.println("Error 2! Wrong ship location! Try again:");
                return false;
            }
            if (Integer.parseInt(next.substring(1)) > this.gameFieldSize) {
                System.out.println("Error 3! Wrong ship location! Try again:");
                return false;
            }
        }
        // Check if the ship doesn't touch any other ships
//        scn = new Scanner(coordinates);
//        while (scn.hasNext()) {
//            String next = scn.next();
//        }
//
        return true;
    }

    private void print() {
        for (int i = 0; i < this.gameFieldSize + 1; ++i) {
            for (int j = 0; j < this.gameFieldSize + 1; ++j) {
                System.out.printf("%s  ", this.gameField[i][j]);
            }
            System.out.println();
        }
    }
}
