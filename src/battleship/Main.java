package battleship;


import java.util.LinkedHashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Create empty game field
        int gameFieldSize = 10;  // max size would be 26 because that many letters in the alphabet
        String gameField[][] = new String[gameFieldSize + 1][gameFieldSize + 1];
        createEmptyGameField(gameField, gameFieldSize);

        printGameField(gameField, gameFieldSize);

        // Add ships
        Scanner scanner = new Scanner(System.in);
        LinkedHashMap<String, Integer> ships = new LinkedHashMap<>();
        ships.put("Aircraft Carrier", 5);
        ships.put("Battleship", 4);
        ships.put("Submarine", 3);
        ships.put("Cruiser", 3);
        ships.put("Destroyer", 2);

        for (String key : ships.keySet()) {
            System.out.println("Enter the coordinates of the " + key + " (" + ships.get(key) + " cells):");
            while (true) {
                System.out.print("> ");
                String answer = scanner.nextLine();
                if (checkCoordinates(answer, gameFieldSize)) break;
            }
        }
    }

    public static void createEmptyGameField(String gameField[][], int size) {
        for (int i = 0; i < size + 1; ++i) {
            for (int j = 0; j < size + 1; ++j) {
                if (i == 0) {
                    if (j == 0) gameField[i][j] = " ";
                    else gameField[i][j] = String.valueOf(j);
                } else {
                    if (j == 0) gameField[i][j] = String.valueOf((char) ('A' + i - 1));
                    else gameField[i][j] = "~";
                }
            }
        }
    }

    public static void printGameField(String gameField[][], int size) {
        for (int i = 0; i < size + 1; ++i) {
            for (int j = 0; j < size + 1; ++j) {
                System.out.printf("%s  ", gameField[i][j]);
            }
            System.out.println();
        }
    }

    public static boolean checkCoordinates(String coordinates, int gameFieldSize) {
        if (!(coordinates.matches("^[A-Z]([1-9]|1\\d|2[0-6])\\s[A-Z]([1-9]|1\\d|2[0-6])$"))) {
            System.out.println("Error 1! Wrong ship location! Try again:");
            return false;
        }

        Scanner scn = new Scanner(coordinates);
        while (scn.hasNext()) {
            String next = scn.next();
            if (next.charAt(0) - 'A' >= gameFieldSize) {
                System.out.println("Error 2! Wrong ship location! Try again:");
                return false;
            }
            if (Integer.parseInt(next.substring(1, next.length())) > gameFieldSize) {
                System.out.println("Error 3! Wrong ship location! Try again:");
                return false;
            }
        }

        return true;
    }
}
